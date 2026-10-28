package com.ecomMicroservices.order.services;

import com.ecomMicroservices.order.clients.ProductServiceClient;
import com.ecomMicroservices.order.clients.UserServiceClient;
import com.ecomMicroservices.order.dto.CartItemRequest;
import com.ecomMicroservices.order.dto.ProductResponse;
import com.ecomMicroservices.order.dto.UserResponse;
import com.ecomMicroservices.order.models.CartItem;
import com.ecomMicroservices.order.repositories.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt =0;

//    @CircuitBreaker(name="productService", fallbackMethod = "addToCartFailBack")
    @Retry(name="retryProductService", fallbackMethod = "addToCartFailBack")
    public Boolean addToCart(Long userId, CartItemRequest request) {

        System.out.println("attempt number : "+ ++attempt);
        ProductResponse productResponse = productServiceClient.getProductById(request.getProductId());
        if(productResponse == null || productResponse.getStock()< request.getQty())
            return false;
        UserResponse userResponse = userServiceClient.fetchUser(userId);
        if(userResponse == null)
            return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem != null){
            existingCartItem.setQty(request.getQty() + existingCartItem.getQty());
            existingCartItem.setPrice(productResponse.getPrice());
            cartItemRepository.save(existingCartItem);
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQty(request.getQty());
            cartItem.setPrice(BigDecimal.valueOf(request.getQty()).multiply(productResponse.getPrice()));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    @Transactional
    public boolean deleteItemFromCart(Long userId, Long productId) {
        CartItem  cartItem= cartItemRepository.findByUserIdAndProductId(userId,productId);
        if(cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

        public List<CartItem> cartItems(Long userId) {
            return cartItemRepository.findByUserId(userId);
        }

        public void clearCart(Long userId) {
            cartItemRepository.deleteByUserId(userId);
        }

        public Boolean addToCartFailBack( Long userId, CartItemRequest request, Exception exception) {

            System.out.println("Fallback called!");
            exception.printStackTrace();
            System.out.println("Reason: " + exception.getMessage());

            return false;
        }
}

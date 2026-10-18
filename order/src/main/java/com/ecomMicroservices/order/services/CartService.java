package com.ecomMicroservices.order.services;

import com.ecomMicroservices.order.dto.CartItemRequest;
import com.ecomMicroservices.order.models.CartItem;
import com.ecomMicroservices.order.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

//    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
//    private final UserRepository userRepository;

    public Boolean addToCart(Long userId, CartItemRequest request) {
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if(productOpt.isEmpty())
//            return false;
//        Product product = productOpt.get();
//        if(product.getStock()< request.getQty())
//            return false;
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty())
//            return false;
//
//        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem != null){
            existingCartItem.setQty(request.getQty() + existingCartItem.getQty());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.0));
            cartItemRepository.save(existingCartItem);
        }else{
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQty(request.getQty());
            cartItem.setPrice(BigDecimal.valueOf(request.getQty()).multiply(BigDecimal.valueOf(1000)));
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
}

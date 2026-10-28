package com.ecomMicroservices.order.controllers;

import com.ecomMicroservices.order.dto.CartItemRequest;
import com.ecomMicroservices.order.models.CartItem;
import com.ecomMicroservices.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart( @RequestHeader("X-User-ID") Long userId, @RequestBody CartItemRequest request){
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Not able to complete request");
        };
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") Long userId,@PathVariable Long productId){
        boolean deleted =  cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
    }

    @GetMapping
    public  ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") Long userId){
        return ResponseEntity.ok(cartService.cartItems(userId));
    }

}

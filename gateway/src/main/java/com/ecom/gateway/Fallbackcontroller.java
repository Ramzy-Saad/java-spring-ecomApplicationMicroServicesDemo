package com.ecom.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class Fallbackcontroller {

    @RequestMapping("/fallback/products")
    public ResponseEntity<List<String>> productsFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Product service is unavailable, try again later"));
    }

    @RequestMapping("/fallback/users")
    public ResponseEntity<List<String>> usersFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("User service is unavailable, try again later"));
    }

    @RequestMapping("/fallback/orders")
    public ResponseEntity<List<String>> ordersFallback(){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Cart & Order service is unavailable, try again later"));
    }
}

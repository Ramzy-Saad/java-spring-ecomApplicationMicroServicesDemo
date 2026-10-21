package com.ecomMicroservices.user.interfaces;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/products")
public interface ProductClient {

    @GetExchange
    String getProducts();
}
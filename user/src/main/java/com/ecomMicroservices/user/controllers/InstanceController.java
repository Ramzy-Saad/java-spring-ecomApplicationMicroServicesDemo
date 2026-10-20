package com.ecomMicroservices.user.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class InstanceController {

    private final RestClient productRestCLient;

    public InstanceController(  @Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder) {
        this.productRestCLient = builder.baseUrl("http://product").build();
    }

    @GetMapping("/getInstancesproducts")
    public Object getInstancesproducts() {
        return productRestCLient.get()
                .uri("/api/products/instances")
                .retrieve()
                .body(Object.class);
    }
}
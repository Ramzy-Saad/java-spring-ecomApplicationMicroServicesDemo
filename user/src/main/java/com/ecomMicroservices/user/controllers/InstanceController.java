package com.ecomMicroservices.user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class InstanceController {

    private final RestClient restClient; // using RestClient to call api
//    private static final String PROVIEDER_URL = "http://localhost:8083";
//    private final RestTemplate restTemplate;  // using RestTemplate to call api

    @GetMapping("/instances")
    public Object getInstance(){
//        Object response = restTemplate.getForObject(PROVIEDER_URL+"/api/products/instances",String.class);
        Object response =restClient.get().uri("/api/products/instances").retrieve().body(Object.class);
        return response;
    }
}

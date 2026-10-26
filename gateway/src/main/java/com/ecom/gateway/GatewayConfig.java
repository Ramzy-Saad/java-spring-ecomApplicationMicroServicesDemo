package com.ecom.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("product-service", r ->
                        r.path("/products/**")
                                .filters(f -> f.rewritePath("/products(?<segment>/?.*)","/api/products${segment}"))
                                .uri("lb://product-service"))
                .route("user-service", r ->
                        r.path("/api/users/**").uri("lb://user-service"))
                .route("order-service", r ->
                        r.path("/api/cart/**","/api/orders/**").uri("lb://order-service"))
                .route("eureka-service", r ->
                        r.path("/eureka/main")
                                .filters(f -> f.setPath("/"))
                                .uri("http://localhost:8761"))
                .route("eureka-server-static", r ->
                        r.path("/eureka/**").uri("http://localhost:8761"))
                .build();
    }
}

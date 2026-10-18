package com.ecomMicroservices.order.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer qty;
}

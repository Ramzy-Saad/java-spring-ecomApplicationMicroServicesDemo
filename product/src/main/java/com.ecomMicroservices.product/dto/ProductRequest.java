package com.ecomMicroservices.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Boolean active;
}

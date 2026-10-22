package com.ecomMicroservices.order.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonPropertyOrder({
        "id",
        "name",
        "desc",
        "price",
        "stock",
        "category",
        "imageUrl",
        "active"
})
public class ProductResponse {
    private Long id;
    private String name;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Boolean active;
}

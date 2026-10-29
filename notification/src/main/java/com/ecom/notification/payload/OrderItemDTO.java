package com.ecom.notification.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private Long productId;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal subTotal;

}

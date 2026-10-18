package com.ecomMicroservices.order.dto;

import com.ecomMicroservices.order.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus = OrderStatus.PENDING;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
}

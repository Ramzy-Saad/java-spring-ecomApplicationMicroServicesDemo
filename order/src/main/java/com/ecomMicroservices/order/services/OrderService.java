package com.ecomMicroservices.order.services;

import com.ecomMicroservices.order.dto.OrderCreatedEvent;
import com.ecomMicroservices.order.dto.OrderItemDTO;
import com.ecomMicroservices.order.dto.OrderResponse;
import com.ecomMicroservices.order.models.OrderItem;
import com.ecomMicroservices.order.models.OrderStatus;
import com.ecomMicroservices.order.models.CartItem;
import com.ecomMicroservices.order.models.Order;
import com.ecomMicroservices.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public Optional<OrderResponse> createOrder(Long userId) {

        //validate cartItems
        List<CartItem> cartItems = cartService.cartItems(userId);
        if (cartItems.isEmpty()){
            return Optional.empty();
        }

        //validate User
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userOptional.get();

        // calc total price
        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems =cartItems.stream()
                .map(cartItem -> new OrderItem(
                   null, cartItem.getProductId(), cartItem.getQty(), cartItem.getPrice(), order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        //clear cart
        cartService.clearCart(userId);
        OrderCreatedEvent eventOrder = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getOrderStatus(),
                mapToOrderItemsDTOs(savedOrder.getItems()),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );
        rabbitTemplate.convertAndSend(exchangeName, routingKey, eventOrder);

        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getItems().stream().map(orderItem -> new OrderItemDTO(
                        orderItem.getId(),
                        orderItem.getProductId(),
                        orderItem.getQty(),
                        orderItem.getPrice(),
                        orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQty()))
                )).toList(),
                order.getCreatedAt()
        );
    }

    private List<OrderItemDTO> mapToOrderItemsDTOs(List<OrderItem> items){
        return items.stream().map(orderItem -> new OrderItemDTO(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getQty(),
                orderItem.getPrice(),
                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQty()))
        )).toList();
    }
}

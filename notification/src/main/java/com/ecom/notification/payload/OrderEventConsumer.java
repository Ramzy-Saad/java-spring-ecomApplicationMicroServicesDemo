package com.ecom.notification.payload;

import com.ecom.notification.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent){
        System.out.println("Recieved Order Event: "+orderEvent);
        System.out.println("Order ID: "+orderEvent.getOrderId());
        System.out.println("Order Status: "+orderEvent.getStatus());
    }
}

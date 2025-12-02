package com.mose.ecommercebackend.orderservice.service;


import com.mose.ecommercebackend.orderservice.dto.OrderRequest;
import com.mose.ecommercebackend.orderservice.event.OrderPlacedEvent;
import com.mose.ecommercebackend.orderservice.model.Order;
import com.mose.ecommercebackend.orderservice.model.OrderItem;
import com.mose.ecommercebackend.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {

        List<OrderItem> items = orderRequest.getItems().stream()
                .map(dto -> OrderItem.builder()
                        .sku(dto.getSku())
                        .price(dto.getPrice())
                        .quantity(dto.getQuantity())
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID())
                .userId(orderRequest.getUserId())
                .orderItems(items)
                .totalPrice(total)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        kafkaTemplate.send("order-placed", new OrderPlacedEvent(order.getOrderNumber().toString(), orderRequest.getUserId().toString()));

        return "Order Placed Successfully";
    }
}
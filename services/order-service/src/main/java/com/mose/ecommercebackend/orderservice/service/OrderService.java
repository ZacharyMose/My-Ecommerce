package com.mose.ecommercebackend.orderservice.service;

import com.mose.ecommercebackend.orderservice.dto.OrderRequest;
import com.mose.ecommercebackend.orderservice.model.Order;
import com.mose.ecommercebackend.orderservice.model.OrderItem;
import com.mose.ecommercebackend.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {

        // Step 1: Validate user exists by calling user-service
        String userUrl =  "http://user-service:8080/api/v1/users/"+ orderRequest.getUserId();
        ResponseEntity<?> response;
        try {
            response = restTemplate.getForEntity(userUrl, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("User not found in user-service with ID: " + orderRequest.getUserId());
        }

        // Step 2: Build order items
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

        // Step 3: Build and save order
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID())
                .userId(orderRequest.getUserId())
                .orderItems(items)
                .totalPrice(total)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        return "Order Placed Successfully";
    }
}

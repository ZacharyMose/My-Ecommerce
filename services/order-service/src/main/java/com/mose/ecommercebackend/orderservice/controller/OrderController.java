package com.mose.ecommercebackend.orderservice.controller;


import com.mose.ecommercebackend.orderservice.dto.OrderRequest;
import com.mose.ecommercebackend.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        // This calls the service logic we wrote earlier
        // 1. Validates User
        // 2. Saves Order
        // 3. Sends Kafka Event
        return orderService.placeOrder(orderRequest);
    }
}

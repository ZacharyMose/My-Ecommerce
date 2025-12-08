package com.mose.ecommercebackend.orderservice.controller;


import com.mose.ecommercebackend.orderservice.dto.OrderRequest;
import com.mose.ecommercebackend.orderservice.service.OrderService;
import jakarta.annotation.security.RunAs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try{
            String result = orderService.placeOrder(orderRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e){
            //Return proper HTTP status if user not found or other eoor
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Sonething ent wrong while placing the order");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok("Order found with id: " + orderId);
    }
}

package com.mose.ecommercebackend.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderNumber;
    private UUID userId;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}

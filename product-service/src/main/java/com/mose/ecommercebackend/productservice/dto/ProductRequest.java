package com.mose.ecommercebackend.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private UUID categoryId;
    private String imageUrl;
}

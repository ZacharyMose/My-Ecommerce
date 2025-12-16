package com.mose.ecommercebackend.productservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryName;
    private String imageUrl;
    private String sku;
}

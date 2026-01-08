package com.mose.ecommercebackend.productservice.controller;

import com.mose.ecommercebackend.productservice.dto.ProductRequest;
import com.mose.ecommercebackend.productservice.dto.ProductResponse;
import com.mose.ecommercebackend.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody String name){
        productService.createCategory(name);
    }
}

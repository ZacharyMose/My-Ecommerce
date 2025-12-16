package com.mose.ecommercebackend.productservice.service;

import com.mose.ecommercebackend.productservice.dto.ProductRequest;
import com.mose.ecommercebackend.productservice.dto.ProductResponse;
import com.mose.ecommercebackend.productservice.exception.CategoryNotFoundException;
import com.mose.ecommercebackend.productservice.exception.ProductNotFoundException;
import com.mose.ecommercebackend.productservice.model.Category;
import com.mose.ecommercebackend.productservice.model.Product;
import com.mose.ecommercebackend.productservice.repository.CategoryRepository;
import com.mose.ecommercebackend.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Category category = null;

        if(productRequest.getCategoryId() != null) {
            category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .category(category)
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .sku(productRequest.getSku())
                .active(true)
                .build();

        productRepository.save(product);
        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return mapToResponse(product);

    }
}

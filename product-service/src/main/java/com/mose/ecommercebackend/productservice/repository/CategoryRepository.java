package com.mose.ecommercebackend.productservice.repository;

import com.mose.ecommercebackend.productservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}

package com.mose.ecommercebackend.userservice.repository;

import com.mose.ecommercebackend.userservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUserId(UUID userId);
}
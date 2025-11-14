package com.mose.ecommercebackend.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private UUID id;
    private String street;
    private String city;
    private String postalCode;
    private String country;
}

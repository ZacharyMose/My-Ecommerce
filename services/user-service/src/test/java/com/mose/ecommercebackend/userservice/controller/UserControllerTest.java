package com.mose.ecommercebackend.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mose.ecommercebackend.userservice.dto.AddressDto;
import com.mose.ecommercebackend.userservice.dto.UserResponse;
import com.mose.ecommercebackend.userservice.model.Role;
import com.mose.ecommercebackend.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetMyProfile() throws Exception {
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "john.doe@example.com", "John", "Doe", Role.ROLE_CUSTOMER);
        when(userService.getMyProfile(any())).thenReturn(userResponse);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(userResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userResponse.getLastName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(userId, "john.doe@example.com", "John", "Doe", Role.ROLE_CUSTOMER);
        when(userService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(userResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userResponse.getLastName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testGetUserAddresses() throws Exception {
        UUID userId = UUID.randomUUID();
        AddressDto addressDto = new AddressDto(UUID.randomUUID(), "123 Main St", "Anytown", "12345", "USA");
        when(userService.isUserOwner(any(), any(UUID.class))).thenReturn(true);
        when(userService.getUserAddresses(userId)).thenReturn(Collections.singletonList(addressDto));

        mockMvc.perform(get("/users/{id}/addresses", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].street").value(addressDto.getStreet()))
                .andExpect(jsonPath("$[0].city").value(addressDto.getCity()))
                .andExpect(jsonPath("$[0].postalCode").value(addressDto.getPostalCode()))
                .andExpect(jsonPath("$[0].country").value(addressDto.getCountry()));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testAddAddress() throws Exception {
        UUID userId = UUID.randomUUID();
        AddressDto addressDto = new AddressDto(UUID.randomUUID(), "123 Main St", "Anytown", "12345", "USA");
        when(userService.isUserOwner(any(), any(UUID.class))).thenReturn(true);
        when(userService.addAddress(any(UUID.class), any(AddressDto.class))).thenReturn(addressDto);

        mockMvc.perform(post("/users/{id}/addresses", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value(addressDto.getStreet()))
                .andExpect(jsonPath("$.city").value(addressDto.getCity()))
                .andExpect(jsonPath("$.postalCode").value(addressDto.getPostalCode()))
                .andExpect(jsonPath("$.country").value(addressDto.getCountry()));
    }
}

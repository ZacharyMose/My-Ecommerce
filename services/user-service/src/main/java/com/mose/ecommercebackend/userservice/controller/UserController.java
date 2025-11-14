package com.mose.ecommercebackend.userservice.controller;

import com.mose.ecommercebackend.userservice.dto.AddressDto;
import com.mose.ecommercebackend.userservice.dto.UserResponse;
import com.mose.ecommercebackend.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getMyProfile(authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/addresses")
    @PreAuthorize("hasRole('ADMIN') or @userService.isUserOwner(authentication, #id)")
    public ResponseEntity<List<AddressDto>> getUserAddresses(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    @PreAuthorize("hasRole('ADMIN') or @userService.isUserOwner(authentication, #id)")
    public ResponseEntity<AddressDto> addAddress(@PathVariable UUID id, @RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(userService.addAddress(id, addressDto), HttpStatus.CREATED);
    }
}
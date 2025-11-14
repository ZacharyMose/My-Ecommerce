package com.mose.ecommercebackend.userservice.service;

import com.mose.ecommercebackend.userservice.dto.AuthResponse;
import com.mose.ecommercebackend.userservice.dto.LoginRequest;
import com.mose.ecommercebackend.userservice.dto.RegisterRequest;
import com.mose.ecommercebackend.userservice.exceptions.EmailAlreadyExistsException;
import com.mose.ecommercebackend.userservice.model.Role;
import com.mose.ecommercebackend.userservice.model.User;
import com.mose.ecommercebackend.userservice.repository.UserRepository;
import com.mose.ecommercebackend.userservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.getEmail());
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER) // Default role
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // This will throw BadCredentialsException if login fails
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If we get here, user is authenticated
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Should not happen if auth succeeded

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(user.getId())
                .build();
    }
}
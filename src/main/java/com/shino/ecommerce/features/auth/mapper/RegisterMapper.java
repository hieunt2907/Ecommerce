package com.shino.ecommerce.features.auth.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.utils.HashPassword;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterMapper {
    private final HashPassword hashPassword;

    public AuthEntity toEntity(RegisterRequest registerRequest) {
        AuthEntity authEntity = new AuthEntity();
        authEntity.setUsername(registerRequest.getUsername());
        authEntity.setEmail(registerRequest.getEmail());
        authEntity.setPassword(hashPassword.hashPassword(registerRequest.getPassword()));
        authEntity.setRole(registerRequest.getRole());
        authEntity.setStatus(registerRequest.getStatus());
        authEntity.setCreatedAt(LocalDateTime.now());
        return authEntity;
    }
}

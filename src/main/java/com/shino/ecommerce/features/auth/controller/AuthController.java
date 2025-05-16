package com.shino.ecommerce.features.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/send-otp")
    public ResponseEntity<String> sendOTP(@RequestBody @Valid RegisterRequest registerRequest) {
        String result = authService.sendOTPRegister(registerRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<String> verifyOTPAndRegister(
            @RequestParam String email,
            @RequestParam String otp) {
        String result = authService.verifyOTPAndRegister(email, otp);
        return ResponseEntity.ok(result);
    }
}

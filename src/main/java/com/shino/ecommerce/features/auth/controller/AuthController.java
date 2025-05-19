package com.shino.ecommerce.features.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.shino.ecommerce.features.auth.dto.request.ForgotPasswordRequest;
import com.shino.ecommerce.features.auth.dto.request.LoginRequest;
import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.dto.request.ResetPasswordRequest;
import com.shino.ecommerce.features.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/send-otp")
    public ResponseEntity<String> sendOTP(@Valid @RequestBody RegisterRequest registerRequest) {
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

    @PostMapping("/login/send-otp")
    public ResponseEntity<String> sendOTPLogin(@Valid @RequestBody LoginRequest loginRequest) {
        String result = authService.sendOTPLogin(loginRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<String> verifyOTPAndLogin(
            @RequestParam String email,
            @RequestParam String otp) {
        String result = authService.verifyOTPAndLogin(email, otp);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<String> sendOTPForgetPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String result = authService.sendOTPForgetPassword(forgotPasswordRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<String> verifyOTPAndForgetPassword(
            @RequestParam String email,
            @RequestParam String otp) {
        String result = authService.verifyOTPAndForgetPassword(email, otp);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String result = authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(result);
    }
}

package com.shino.ecommerce.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shino.ecommerce.features.user.dto.request.*;
import com.shino.ecommerce.features.user.dto.response.*;
import com.shino.ecommerce.features.user.services.authentication.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register/request")
    public ResponseEntity<AuthenticationResponse> requestRegister(
            @Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(authenticationService.requestRegister(request));
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login/request")
    public ResponseEntity<AuthenticationResponse> requestLogin(
            @Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(authenticationService.requestLogin(request));
    }

    @PostMapping("/login/confirm")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<AuthenticationResponse> forgotPassword(
            @RequestParam String email) {
        return ResponseEntity.ok(authenticationService.forgotPassword(email));
    }

    @PostMapping("/password/verify")
    public ResponseEntity<AuthenticationResponse> verifyResetPassword(
            @RequestBody OtpVerificationRequest otpVerificationRequest) {
        return ResponseEntity.ok(authenticationService.verifyResetPassword(otpVerificationRequest));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<AuthenticationResponse> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logout(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.logout(request));
    }
}

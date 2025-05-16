package com.shino.ecommerce.features.auth.service;

import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;

public interface AuthService {
    String sendOTPRegister(RegisterRequest registerRequest);
    String verifyOTPAndRegister(String email, String otp);
}

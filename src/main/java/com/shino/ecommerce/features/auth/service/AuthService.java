package com.shino.ecommerce.features.auth.service;

import com.shino.ecommerce.features.auth.dto.request.LoginRequest;
import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;

public interface AuthService {
    String sendOTPRegister(RegisterRequest registerRequest);
    String verifyOTPAndRegister(String email, String otp);
    String sendOTPLogin(LoginRequest loginRequest);
    String verifyOTPAndLogin(String email, String otp);
}

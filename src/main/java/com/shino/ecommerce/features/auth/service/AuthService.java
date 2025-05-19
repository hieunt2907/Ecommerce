package com.shino.ecommerce.features.auth.service;

import com.shino.ecommerce.features.auth.dto.request.ForgotPasswordRequest;
import com.shino.ecommerce.features.auth.dto.request.LoginRequest;
import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.dto.request.ResetPasswordRequest;

public interface AuthService {
    String sendOTPRegister(RegisterRequest registerRequest);
    String verifyOTPAndRegister(String email, String otp);
    String sendOTPLogin(LoginRequest loginRequest);
    String verifyOTPAndLogin(String email, String otp);
    String sendOTPForgetPassword(ForgotPasswordRequest forgotPasswordRequest);
    String verifyOTPAndForgetPassword(String email, String otp);
    String resetPassword(ResetPasswordRequest resetPasswordRequest);
}

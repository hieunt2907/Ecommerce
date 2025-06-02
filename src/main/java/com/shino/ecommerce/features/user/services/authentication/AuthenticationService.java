package com.shino.ecommerce.features.user.services.authentication;

import com.shino.ecommerce.features.user.dto.request.*;
import com.shino.ecommerce.features.user.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse requestRegister(UserCreateRequest request);
    AuthenticationResponse register(OtpVerificationRequest request);
    AuthenticationResponse requestLogin(UserLoginRequest request);
    LoginResponse login(OtpVerificationRequest request);
    AuthenticationResponse forgotPassword(String email);
    AuthenticationResponse verifyResetPassword(OtpVerificationRequest otpVerificationRequest);
    AuthenticationResponse resetPassword(PasswordResetRequest request);
    AuthenticationResponse logout(HttpServletRequest request);
}

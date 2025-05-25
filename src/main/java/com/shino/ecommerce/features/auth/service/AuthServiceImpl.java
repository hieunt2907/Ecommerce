package com.shino.ecommerce.features.auth.service;

import org.springframework.stereotype.Service;


import com.shino.ecommerce.features.auth.dto.request.ForgotPasswordRequest;
import com.shino.ecommerce.features.auth.dto.request.LoginRequest;
import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.dto.request.ResetPasswordRequest;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.mapper.RegisterMapper;
import com.shino.ecommerce.features.auth.repository.AuthRepository;
import com.shino.ecommerce.features.auth.utils.HashPassword;
import com.shino.ecommerce.features.auth.utils.JwtToken;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final EmailService emailService;
    private final OTPService otpService;
    private final RegisterMapper registerMapper;
    private final Map<String, RegisterRequest> tempRegisterStorage = new HashMap<>();
    private final HashPassword hashPassword;
    private final JwtToken jwtToken;

    @Override
    public String sendOTPRegister(RegisterRequest registerRequest) {
        try {
            if (authRepository.existsByUsername(registerRequest.getUsername())) {
                return "Username already exists";
            }
            if (authRepository.existsByEmail(registerRequest.getEmail())) {
                return "Email already exists";
            }

            String otp = otpService.generateOTP();
            otpService.saveOTP(registerRequest.getEmail(), otp);
            emailService.sendVerificationEmailAsync(registerRequest.getEmail(), otp);
            
            tempRegisterStorage.put(registerRequest.getEmail(), registerRequest);
            
            return "OTP sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    @Override
    public String verifyOTPAndRegister(String email, String otp) {
        RegisterRequest savedRequest = tempRegisterStorage.get(email);
        if (savedRequest == null) {
            return "Registration request not found";
        }

        if (otpService.validateOTP(email, otp)) {
            try {
                AuthEntity authEntity = registerMapper.toEntity(savedRequest);
                authRepository.save(authEntity);
                tempRegisterStorage.remove(email);
                return "Registration successful";
            } catch (Exception e) {
                e.printStackTrace();
                return "Registration failed";
            }
        }
        return "Invalid OTP";
    }

    @Override
    public String sendOTPLogin(LoginRequest loginRequest) {
        try {
            AuthEntity authEntity = authRepository.findByEmail(loginRequest.getEmail());
            if (authEntity == null) {
                return "User not found";
            }
            // Fix: Use verifyPassword method instead of direct comparison
            if (!hashPassword.verifyPassword(loginRequest.getPassword(), authEntity.getPassword())) {
                return "Invalid password";
            }
            String otp = otpService.generateOTP();
            otpService.saveOTP(loginRequest.getEmail(), otp);
            emailService.sendVerificationEmailAsync(loginRequest.getEmail(), otp);
            
            return "OTP sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    @Override
    public String verifyOTPAndLogin(String email, String otp) {
        if (otpService.validateOTP(email, otp)) {
            try {
                AuthEntity authEntity = authRepository.findByEmail(email);
                if (authEntity == null) {
                    return "User not found";
                }
                // Perform login logic here, e.g., generate JWT token
                // For simplicity, we just return a success message
                // In a real application, you would generate a JWT token here
                String token = jwtToken.generateToken(authEntity);
                return "Login successful, token: " + token;
            } catch (Exception e) {
                e.printStackTrace();
                return "Login failed";
            }
        }
        return "Invalid OTP";

        
    }

    @Override
    public String sendOTPForgetPassword(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            AuthEntity authEntity = authRepository.findByEmail(forgotPasswordRequest.getEmail());
            if (authEntity == null) {
                return "User not found";
            }
            String otp = otpService.generateOTP();
            otpService.saveOTP(forgotPasswordRequest.getEmail(), otp);
            emailService.sendVerificationEmailAsync(forgotPasswordRequest.getEmail(), otp);
            
            return "OTP sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    @Override
    public String verifyOTPAndForgetPassword(String email, String otp) {
        if (otpService.validateOTP(email, otp)) {
            try {
                AuthEntity authEntity = authRepository.findByEmail(email);
                if (authEntity == null) {
                    return "User not found";
                }
                // Perform password reset logic here
                return "Now you can reset your password";
            } catch (Exception e) {
                e.printStackTrace();
                return "You can not reset your password";
            }
        }
        return "Invalid OTP";
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        try {
            AuthEntity authEntity = authRepository.findByEmail(resetPasswordRequest.getEmail());
            if (authEntity == null) {
                return "User not found";
            }
            if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
                return "Passwords do not match";
            }
            String hashedPassword = hashPassword.hashPassword(resetPasswordRequest.getNewPassword());
            authEntity.setPassword(hashedPassword);
            authRepository.save(authEntity);
            return "Password reset successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to reset password";
        }
    }

    
}

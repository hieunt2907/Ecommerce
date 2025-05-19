package com.shino.ecommerce.features.auth.service;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.auth.dto.request.LoginRequest;
import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.mapper.RegisterMapper;
import com.shino.ecommerce.features.auth.repository.AuthRepository;
import com.shino.ecommerce.features.auth.utils.HashPassword;

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
            emailService.sendVerificationEmail(registerRequest.getEmail(), otp);
            
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
            emailService.sendVerificationEmail(loginRequest.getEmail(), otp);
            
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
                return "Login successful";
            } catch (Exception e) {
                e.printStackTrace();
                return "Login failed";
            }
        }
        return "Invalid OTP";
    }
}

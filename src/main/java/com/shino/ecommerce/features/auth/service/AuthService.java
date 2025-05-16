package com.shino.ecommerce.features.auth.service;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.auth.dto.request.RegisterRequest;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.mapper.RegisterMapper;
import com.shino.ecommerce.features.auth.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final EmailService emailService;
    private final OTPService otpService;
    private final RegisterMapper registerMapper;
    private final Map<String, RegisterRequest> tempRegisterStorage = new HashMap<>();

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
            
            // Store register request temporarily
            tempRegisterStorage.put(registerRequest.getEmail(), registerRequest);
            
            return "OTP sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send OTP";
        }
    }

    public String verifyOTPAndRegister(String email, String otp) {
        RegisterRequest savedRequest = tempRegisterStorage.get(email);
        if (savedRequest == null) {
            return "Registration request not found";
        }

        if (otpService.validateOTP(email, otp)) {
            try {
                AuthEntity authEntity = registerMapper.toEntity(savedRequest);
                authRepository.save(authEntity);
                // Remove temp data after successful registration
                tempRegisterStorage.remove(email);
                return "Registration successful";
            } catch (Exception e) {
                e.printStackTrace();
                return "Registration failed";
            }
        }
        return "Invalid OTP";
    }
}

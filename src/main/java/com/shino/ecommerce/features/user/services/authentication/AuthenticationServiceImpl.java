package com.shino.ecommerce.features.user.services.authentication;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shino.ecommerce.common.messaging.dto.EmailDTO;
import com.shino.ecommerce.common.messaging.service.EmailService;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.repository.UserRepository;
import com.shino.ecommerce.features.user.services.user.Userservice;
import com.shino.ecommerce.features.user.utils.EmailSessionUtil;
import com.shino.ecommerce.features.user.utils.HashPassword;
import com.shino.ecommerce.features.user.utils.OtpSend;
import com.shino.ecommerce.security.JwtUtil;
import com.shino.ecommerce.security.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import com.shino.ecommerce.features.user.dto.response.*;
import com.shino.ecommerce.features.user.dto.request.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final HashPassword hashPassword;
    private final Userservice userService;
    private final OtpSend otpSend;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailSessionUtil emailSessionUtil;

    @Override
    public AuthenticationResponse requestRegister(UserCreateRequest request) {
        try {
            if (userRepository.existsByEmailAndUsername(request.getEmail(), request.getUsername())) {
                throw new RuntimeException("Email or username already exists");
            }
            String sessionId = emailSessionUtil.createEmailSession(request.getEmail());
            String otp = otpSend.generateOTP();
            otpSend.saveOTP(request.getEmail(), otp);
            EmailDTO emailDTO = new EmailDTO(request.getEmail(), "Registration OTP",
                    "Your OTP for registration is: " + otp);
            emailService.sendEmailAsync(emailDTO);
            redisTemplate.opsForValue().set(sessionId, request);
            return new AuthenticationResponse(sessionId, "OTP sent successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error requesting registration: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse register(OtpVerificationRequest request) {
        try {
            String email = emailSessionUtil.getEmailFromSession(request.getSessionId());
            UserCreateRequest userCreateRequest = (UserCreateRequest) redisTemplate.opsForValue()
                    .get(request.getSessionId());

            if (!otpSend.validateOTP(email, request.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }

            userService.createUser(userCreateRequest);
            redisTemplate.delete(request.getSessionId());
            emailSessionUtil.invalidateSession(request.getSessionId());

            return new AuthenticationResponse(request.getSessionId(), "Registration successful");
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse requestLogin(UserLoginRequest request) {
        try {
            UserEntity user = null;
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                user = userRepository.findByEmail(request.getEmail());
            } else if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                user = userRepository.findByUsername(request.getUsername());
            }
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            if (!user.getIsActive()) {
                throw new RuntimeException("User account is not active");
            }

            if (!hashPassword.verifyPassword(request.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("Incorrect password");
            }
            String sessionId = emailSessionUtil.createEmailSession(user.getEmail());
            String otp = otpSend.generateOTP();
            otpSend.saveOTP(user.getEmail(), otp);
            EmailDTO emailDTO = new EmailDTO(user.getEmail(), "Login OTP",
                    "Your OTP for login is: " + otp);
            emailService.sendEmailAsync(emailDTO);
            return new AuthenticationResponse(sessionId, "OTP sent successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error requesting login: " + e.getMessage(), e);
        }
    }

    @Override
    public LoginResponse login(OtpVerificationRequest request) {
        try {
            String email = emailSessionUtil.getEmailFromSession(request.getSessionId());
            if (!otpSend.validateOTP(email, request.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            String token = jwtUtil.generateToken(user.getUsername(),
                    user.getRoles().stream().map(role -> role.getRoleName()).toList());
            emailSessionUtil.invalidateSession(request.getSessionId());
            return new LoginResponse(token, "Login successful");
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage(), e);
        }
    }



    @Override
    public AuthenticationResponse forgotPassword(String email) {
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            String sessionId = emailSessionUtil.createEmailSession(email);
            String otp = otpSend.generateOTP();
            otpSend.saveOTP(email, otp);
            EmailDTO emailDTO = new EmailDTO(email, "Password Reset OTP",
                    "Your OTP for password reset is: " + otp);
            emailService.sendEmailAsync(emailDTO);
            return new AuthenticationResponse(sessionId, "OTP sent successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error requesting password reset: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse verifyResetPassword(OtpVerificationRequest otpVerificationRequest) {
        try {
            String email = emailSessionUtil.getEmailFromSession(otpVerificationRequest.getSessionId());
            UserEntity userEntity = userRepository.findByEmail(email);
            if (!otpSend.validateOTP(email, otpVerificationRequest.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }
            if (userEntity == null) {
                throw new RuntimeException("User not found");
            }

            return new AuthenticationResponse(otpVerificationRequest.getSessionId(),
                    "Verify request reset rassword successfully");

        } catch (Exception e) {
            throw new RuntimeException("Error verify reset password" + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse resetPassword(PasswordResetRequest request) {
        try {
            String email = emailSessionUtil.getEmailFromSession(request.getSessionId());
            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            user.setPasswordHash(hashPassword.hashPassword(request.getNewPassword()));
            userRepository.save(user);
            emailSessionUtil.invalidateSession(request.getSessionId());
            return new AuthenticationResponse(request.getSessionId(), "Password reset successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error resetting password: " + e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationResponse logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
        long expirationInSeconds = jwtUtil.getExpirationDurationInSeconds(token);
        tokenBlacklistService.blacklistToken(token, expirationInSeconds);

        return new AuthenticationResponse(null, "Logout successfully");
    }

}
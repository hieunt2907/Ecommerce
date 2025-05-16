package com.shino.ecommerce.features.auth.service;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration OTP_EXPIRATION = Duration.ofMinutes(5);

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void saveOTP(String email, String otp) {
        redisTemplate.opsForValue().set(getKey(email), otp, OTP_EXPIRATION);
    }

    public boolean validateOTP(String email, String otp) {
        String savedOTP = redisTemplate.opsForValue().get(getKey(email));
        if (savedOTP != null && savedOTP.equals(otp)) {
            redisTemplate.delete(getKey(email));
            return true;
        }
        return false;
    }

    private String getKey(String email) {
        return "registration:otp:" + email;
    }
}

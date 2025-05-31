package com.shino.ecommerce.features.user.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmailSessionUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String EMAIL_SESSION_PREFIX = "email_session:";
    
    @Value("${app.security.email-session-expiration:300}") // 5 minutes default
    private long sessionExpirationSeconds;

    public EmailSessionUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createEmailSession(String email) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
            EMAIL_SESSION_PREFIX + sessionId, 
            email, 
            sessionExpirationSeconds, 
            java.util.concurrent.TimeUnit.SECONDS
        );
        return sessionId;
    }

    public String getEmailFromSession(String sessionId) {
        String email = redisTemplate.opsForValue().get(EMAIL_SESSION_PREFIX + sessionId);
        if (email == null) {
            throw new RuntimeException("Email session expired or invalid");
        }
        return email;
    }

    public void invalidateSession(String sessionId) {
        redisTemplate.delete(EMAIL_SESSION_PREFIX + sessionId);
    }
}

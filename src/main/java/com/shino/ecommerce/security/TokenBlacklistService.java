package com.shino.ecommerce.security;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final String BLACKLIST_PREFIX = "blacklisted_token:";
    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationInSeconds) {
        redisTemplate.opsForValue().set(
            BLACKLIST_PREFIX + token,
            "true",
            Duration.ofSeconds(expirationInSeconds)
        );
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}

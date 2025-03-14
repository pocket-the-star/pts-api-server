package com.pts.api.user.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "RT:";
    private static final long REFRESH_TOKEN_EXPIRATION = 30L * 24L * 60L * 60L * 1000L;

    public void save(Long userId, String token) {
        String key = generateKey(userId);
        redisTemplate.opsForValue()
            .set(key, token, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public Optional<String> findOneById(Long userId) {
        String key = generateKey(userId);
        String value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value);
    }

    public void deleteById(Long userId) {
        String key = generateKey(userId);
        redisTemplate.delete(key);
    }

    private String generateKey(Long userId) {
        return KEY_PREFIX + userId;
    }
} 
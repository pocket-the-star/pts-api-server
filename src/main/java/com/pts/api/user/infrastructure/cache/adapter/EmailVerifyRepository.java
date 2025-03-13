package com.pts.api.user.infrastructure.cache.adapter;

import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.infrastructure.mapper.IEmailVerifyMapper;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerifyRepository implements EmailVerifyRepositoryPort {

    private final StringRedisTemplate redisTemplate;
    private final IEmailVerifyMapper emailVerifyMapper;
    private static final String KEY_PREFIX = "EA:";
    private static final String LOCK_KEY_PREFIX = "EALOCK:";
    private static final long EMAIL_AUTH_EXPIRATION = 10L * 60L * 1000L;
    private static final long EMAIL_AUTH_LOCK_EXPIRATION = 5L * 1000L;

    @Override
    public void save(EmailVerify emailAuth) {
        redisTemplate
            .opsForValue()
            .set(
                generateKey(emailAuth.getEmail()),
                convertValue(emailAuth),
                EMAIL_AUTH_EXPIRATION,
                TimeUnit.MILLISECONDS
            );
    }

    @Override
    public Optional<EmailVerify> findById(String email) {
        String value = redisTemplate.opsForValue().get(generateKey(email));
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(convertValue(value));
    }

    @Override
    public void deleteById(String email) {
        String key = generateKey(email);
        redisTemplate.delete(key);
    }

    @Override
    public boolean getLock(String email) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue()
            .setIfAbsent(generateLockKey(email), "lock", EMAIL_AUTH_LOCK_EXPIRATION,
                TimeUnit.MILLISECONDS));
    }

    @Override
    public void releaseLock(String email) {
        redisTemplate.delete(generateLockKey(email));
    }

    private String generateKey(String email) {
        return KEY_PREFIX + email;
    }

    private String generateLockKey(String email) {
        return LOCK_KEY_PREFIX + email;
    }

    private EmailVerify convertValue(String value) {
        return emailVerifyMapper.mapToObject(value);
    }

    private String convertValue(EmailVerify emailAuth) {
        return emailVerifyMapper.mapToString(emailAuth);
    }
}

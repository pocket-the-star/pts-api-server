package com.pts.api.user.infrastructure.cache.adapter;

import com.pts.api.lib.internal.shared.util.serializer.DataSerializer;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.infrastructure.cache.model.EmailVerifyEntity;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerifyRepositoryAdapter implements EmailVerifyRepositoryPort {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "EA:";
    private static final String LOCK_KEY_PREFIX = "EALOCK:";
    private static final long EMAIL_AUTH_EXPIRATION = 10L * 60L * 1000L;
    private static final long EMAIL_AUTH_LOCK_EXPIRATION = 5L * 1000L;

    @Override
    public void save(EmailVerify emailAuth) {
        EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.fromDomain(emailAuth);
        redisTemplate
            .opsForValue()
            .set(
                generateKey(emailVerifyEntity.getEmail()),
                convertValue(emailVerifyEntity),
                EMAIL_AUTH_EXPIRATION,
                TimeUnit.MILLISECONDS
            );
    }

    @Override
    public Optional<EmailVerify> findByEmail(String email) {
        String value = redisTemplate.opsForValue().get(generateKey(email));
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(convertValue(value).toDomain());
    }

    @Override
    public void deleteByEmail(String email) {
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

    private EmailVerifyEntity convertValue(String value) {
        return DataSerializer.deserialize(value, EmailVerifyEntity.class);
    }

    private String convertValue(EmailVerifyEntity emailAuth) {
        return DataSerializer.serialize(emailAuth);
    }
} 
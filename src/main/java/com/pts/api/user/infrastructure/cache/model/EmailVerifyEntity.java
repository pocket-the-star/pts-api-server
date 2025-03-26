package com.pts.api.user.infrastructure.cache.model;

import com.pts.api.user.domain.model.EmailVerify;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "email_verify", timeToLive = 600)
public class EmailVerifyEntity {

    @Id
    private String email;
    private String code;
    private int tryCount;
    private boolean verified;

    @Builder
    public EmailVerifyEntity(String email, String code, int tryCount, boolean verified) {
        this.email = email;
        this.code = code;
        this.tryCount = tryCount;
        this.verified = verified;
    }

    public EmailVerify toDomain() {
        return EmailVerify.builder()
            .email(email)
            .code(code)
            .tryCount(tryCount)
            .verified(verified)
            .build();
    }

    public static EmailVerifyEntity fromDomain(EmailVerify emailVerify) {
        return EmailVerifyEntity.builder()
            .email(emailVerify.getEmail())
            .code(emailVerify.getCode())
            .tryCount(emailVerify.getTryCount())
            .verified(emailVerify.isVerified())
            .build();
    }
}
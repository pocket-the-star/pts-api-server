package com.pts.api.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "email_verify", timeToLive = 600)
public class EmailVerify {

    @Id
    private String email;
    private String authCode;
    private int tryCount;
    private boolean verified;

    @Builder
    public EmailVerify(String email, String authCode, int tryCount, boolean verified) {
        this.email = email;
        this.authCode = authCode;
        this.tryCount = tryCount;
        this.verified = verified;
    }

    public void incrementTryCount() {
        this.tryCount++;
    }

    public boolean isOverTryCount() {
        int MAX_TRY_COUNT = 3;
        return this.tryCount >= MAX_TRY_COUNT;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }
} 
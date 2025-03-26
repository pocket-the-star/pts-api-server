package com.pts.api.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerify {

    private String email;
    private String code;
    private boolean verified;
    private int tryCount;

    @Builder
    public EmailVerify(
        String email, String code, boolean verified, int tryCount) {
        this.email = email;
        this.code = code;
        this.verified = verified;
        this.tryCount = tryCount;
    }

    public void incrementTryCount() {
        this.tryCount++;
    }

    public boolean isOverTryCount() {
        int MAX_TRY_COUNT = 3;
        return this.tryCount >= MAX_TRY_COUNT;
    }

    public void verifyTrue() {
        this.verified = true;
    }
}
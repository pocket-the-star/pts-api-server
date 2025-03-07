package com.pts.api.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerify {

    private String email;
    private String authCode;
    private int tryCount;
    private boolean isVerified;

    @Builder
    public EmailVerify(String email, String authCode, int tryCount, boolean isVerified) {
        this.email = email;
        this.authCode = authCode;
        this.tryCount = tryCount;
        this.isVerified = isVerified;
    }

    public void updateTryCount() {
        this.tryCount++;
    }

    public boolean isOverTryCount() {
        int MAX_TRY_COUNT = 3;
        return this.tryCount >= MAX_TRY_COUNT;
    }

    public boolean isMatchAuthCode(String authCode) {
        return this.authCode.equals(authCode);
    }

    public boolean isMatchEmail(String email) {
        return this.email.equals(email);
    }

    public void verify() {
        this.isVerified = true;
    }
}

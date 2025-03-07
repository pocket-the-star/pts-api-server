package com.pts.api.user.common.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class EmailVerifyLockedException extends CustomBusinessException {

    public EmailVerifyLockedException(String message) {
        super(message, HttpStatus.LOCKED);
    }
}

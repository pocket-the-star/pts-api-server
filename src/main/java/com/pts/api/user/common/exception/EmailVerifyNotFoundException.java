package com.pts.api.user.common.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class EmailVerifyNotFoundException extends CustomBusinessException {

    public EmailVerifyNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

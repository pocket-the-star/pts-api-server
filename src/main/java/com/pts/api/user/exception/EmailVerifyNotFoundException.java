package com.pts.api.user.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class EmailVerifyNotFoundException extends CustomBusinessException {

    public EmailVerifyNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

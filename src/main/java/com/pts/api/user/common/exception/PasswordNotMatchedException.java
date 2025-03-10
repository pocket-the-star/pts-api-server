package com.pts.api.user.common.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends CustomBusinessException {

    public PasswordNotMatchedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

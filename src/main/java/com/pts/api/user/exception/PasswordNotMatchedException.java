package com.pts.api.user.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends CustomBusinessException {

    public PasswordNotMatchedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

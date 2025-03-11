package com.pts.api.global.presentation.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomBusinessException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

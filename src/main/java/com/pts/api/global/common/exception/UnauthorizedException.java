package com.pts.api.global.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomBusinessException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

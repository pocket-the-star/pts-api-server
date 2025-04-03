package com.pts.api.global.common.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomBusinessException {

    public UnAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

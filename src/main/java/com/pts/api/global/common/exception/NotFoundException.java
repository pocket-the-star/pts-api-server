package com.pts.api.global.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomBusinessException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}

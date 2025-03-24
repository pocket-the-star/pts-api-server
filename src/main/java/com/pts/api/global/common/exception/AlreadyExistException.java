package com.pts.api.global.common.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends CustomBusinessException {

    public AlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}

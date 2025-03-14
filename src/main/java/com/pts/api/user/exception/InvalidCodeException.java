package com.pts.api.user.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidCodeException extends CustomBusinessException {

    public InvalidCodeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

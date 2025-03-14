package com.pts.api.user.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends CustomBusinessException {

    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

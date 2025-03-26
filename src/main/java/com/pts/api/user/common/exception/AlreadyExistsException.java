package com.pts.api.user.common.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends CustomBusinessException {

    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

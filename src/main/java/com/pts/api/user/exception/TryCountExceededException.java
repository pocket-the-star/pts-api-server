package com.pts.api.user.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class TryCountExceededException extends CustomBusinessException {

    public TryCountExceededException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

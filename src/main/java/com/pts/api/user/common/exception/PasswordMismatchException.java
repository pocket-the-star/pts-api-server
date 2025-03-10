package com.pts.api.user.common.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends CustomBusinessException {

    public PasswordMismatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}

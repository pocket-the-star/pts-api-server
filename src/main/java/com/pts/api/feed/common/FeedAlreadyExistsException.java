package com.pts.api.feed.common.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class FeedAlreadyExistsException extends CustomBusinessException {

    public FeedAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}

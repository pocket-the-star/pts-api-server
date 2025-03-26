package com.pts.api.product.common.exception;

import com.pts.api.global.common.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomBusinessException {

    public ProductNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}

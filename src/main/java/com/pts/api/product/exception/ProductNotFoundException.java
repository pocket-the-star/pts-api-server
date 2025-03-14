package com.pts.api.product.exception;

import com.pts.api.global.presentation.exception.CustomBusinessException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomBusinessException {

    public ProductNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}

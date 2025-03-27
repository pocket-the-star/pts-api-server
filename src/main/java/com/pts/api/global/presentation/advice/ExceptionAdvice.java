package com.pts.api.global.presentation.advice;

import com.pts.api.global.common.exception.CustomBusinessException;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomBusinessException.class)
    public ResponseEntity<BaseResponse<Object>> handleCustomBusinessException(
        CustomBusinessException e) {
        log(e);
        return getExceptionResponse(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception e) {
        log(e);
        return getExceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<BaseResponse<Object>> getExceptionResponse(String message,
        HttpStatus status) {
        return ResponseGenerator.fail(message, status);
    }

    private void log(Exception e) {
        StackTraceElement element = e.getStackTrace()[0];
        log.error("클래스: {}, 메시지: {}",
            element.getClassName(),
            e.toString());
//        log.error("클래스: {}, 메서드: {}, 라인: {}, 예외: {}, 메시지: {}",
//            element.getClassName(),
//            element.getMethodName(),
//            element.getLineNumber(),
//            e.getClass(),
//            e.getMessage());
    }
}

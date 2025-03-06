package com.pts.api.global.presentation.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class ResponseGenerator {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public static <T> ResponseEntity<BaseResponse<T>> ok(ResponseMsg resultMsg, HttpStatus status,
        T data) {
        return ResponseEntity.status(status.value())
            .body(genResponse(resultMsg.getValue(), status, data));
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(ResponseMsg resultMsg, HttpStatus status) {
        return ResponseEntity.status(status.value())
            .body(genResponse(resultMsg.getValue(), status, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> fail(String resultMsg, HttpStatus status) {
        return ResponseEntity.status(status.value())
            .body(genResponse(resultMsg, status, null));
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(ResponseMsg resultMsg, HttpStatus status,
        T data, String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME,
                refreshToken)
            .httpOnly(true)
            .path("/")
            .maxAge(30L * 24L * 60L * 60L)
            .sameSite("Strict")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(genResponse(resultMsg.getValue(), status, data));
    }

    private static <T> BaseResponse<T> genResponse(String resultMsg, HttpStatus status, T data) {
        return new BaseResponse<>(resultMsg, status.value(), data);
    }
} 
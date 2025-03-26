package com.pts.api.user.application.dto.response;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {

}
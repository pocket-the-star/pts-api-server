package com.pts.api.user.dto.response;

public record TokenResponseDto(
    String accessToken,
    String refreshToken
) {} 
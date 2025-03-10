package com.pts.api.user.application.dto.request;


public record SignUpRequestDto(
    String userRole,
    String nickname,
    String email,
    String password,
    String passwordConfirm
) {

}

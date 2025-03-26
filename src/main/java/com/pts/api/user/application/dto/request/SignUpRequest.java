package com.pts.api.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
    @NotNull
    String userRole,
    @NotNull
    String nickname,
    @NotNull
    @Email
    String email,
    @NotNull
    String password,
    @NotNull
    String passwordConfirm
) {

}
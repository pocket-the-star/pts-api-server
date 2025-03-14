package com.pts.api.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignUpRequestDto(
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
) {} 
package com.pts.api.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
    @Email
    @NotNull
    String email,
    @NotNull
    String password
) {

}

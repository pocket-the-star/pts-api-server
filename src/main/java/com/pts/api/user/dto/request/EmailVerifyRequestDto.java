package com.pts.api.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailVerifyRequestDto(
    @Email
    @NotNull
    String email
) {} 
package com.pts.api.user.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TokenResponseDto(String accessToken, @JsonIgnore String refreshToken) {

}

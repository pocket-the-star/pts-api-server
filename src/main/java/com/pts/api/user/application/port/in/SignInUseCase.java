package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.SignInRequestDto;
import com.pts.api.user.application.dto.response.TokenResponseDto;

public interface SignInUseCase {

    TokenResponseDto execute(SignInRequestDto request);

}

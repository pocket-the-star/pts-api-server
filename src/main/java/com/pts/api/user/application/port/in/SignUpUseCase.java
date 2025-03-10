package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.SignUpRequestDto;

public interface SignUpUseCase {

    void execute(SignUpRequestDto signUpRequestDto);

}

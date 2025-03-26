package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.SignUpRequest;

public interface SignUpUseCase {

    void signUp(SignUpRequest request);
}

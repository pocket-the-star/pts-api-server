package com.pts.api.user.application.port.in;

import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.response.TokenResponse;

public interface SignInUseCase {

    TokenResponse signIn(SignInRequest request);
}

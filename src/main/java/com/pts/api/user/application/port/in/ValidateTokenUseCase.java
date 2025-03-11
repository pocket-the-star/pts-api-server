package com.pts.api.user.application.port.in;

import com.pts.api.user.domain.model.TokenPayload;

public interface ValidateTokenUseCase {

    TokenPayload execute(String bearerToken, String refreshToken);

}

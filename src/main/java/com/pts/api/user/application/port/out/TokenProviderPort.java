package com.pts.api.user.application.port.out;

import com.pts.api.user.domain.model.TokenPayload;

public interface TokenProviderPort {

    String create(TokenPayload payload, Long expireTime);

    TokenPayload parseToken(String token);
}

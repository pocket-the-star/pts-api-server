package com.pts.api.user.application.port.out;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import io.jsonwebtoken.Claims;

public interface TokenProviderPort {


    String create(Long userId, UserRole userRole, TokenType tokenType, Long expireTime);

    Claims parseToken(String token);

    Long getUserId(String token);

    UserRole getUserRole(String token);

    TokenType getTokenType(String token);
}

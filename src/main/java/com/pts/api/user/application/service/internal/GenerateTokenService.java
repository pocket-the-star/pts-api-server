package com.pts.api.user.application.service.internal;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.domain.model.TokenPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateTokenService implements IGenerateTokenService {

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public TokenResponseDto generate(Long userId, UserRole userRole) {

        return new TokenResponseDto(
            create(userId, TokenType.ACCESS, userRole, ACCESS_TOKEN_EXPIRE_TIME),
            create(userId, TokenType.REFRESH, userRole, REFRESH_TOKEN_EXPIRE_TIME)
        );
    }

    private String create(Long userId, TokenType tokenType, UserRole userRole, Long expireTime) {
        return tokenProviderPort.create(getPayload(userId, userRole, tokenType), expireTime);
    }

    private TokenPayload getPayload(Long userId, UserRole userRole, TokenType tokenType) {
        return TokenPayload.builder()
            .userId(userId)
            .userRole(userRole)
            .tokenType(tokenType)
            .build();
    }
}

package com.pts.api.user.application.service;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.user.application.port.in.ValidateTokenUseCase;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.common.exception.InvalidTokenException;
import com.pts.api.user.domain.model.TokenPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenService implements ValidateTokenUseCase {

    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public TokenPayload execute(String bearerToken, String refreshToken) {
        String accessToken = extractToken(bearerToken);
        TokenPayload tokenPayload = validateAccessToken(accessToken);
        validateRefreshToken(tokenPayload.getUserId(), refreshToken);

        return tokenPayload;
    }

    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new InvalidTokenException("유효하지 않은 토큰 형식입니다");
    }

    private TokenPayload validateAccessToken(String accessToken) {
        TokenPayload payload = tokenProviderPort.parseToken(accessToken);
        if (payload.getTokenType() != TokenType.ACCESS) {
            throw new InvalidTokenException("Access Token이 아닙니다");
        }

        return payload;
    }

    private void validateRefreshToken(Long userId, String refreshToken) {
        String storedRefreshToken = refreshTokenRepositoryPort.findOneById(userId)
            .orElseThrow(() -> new InvalidTokenException("저장된 Refresh Token이 없습니다"));

        if (!storedRefreshToken.equals(refreshToken)) {
            refreshTokenRepositoryPort.deleteById(userId);
            throw new InvalidTokenException("Refresh Token이 일치하지 않습니다");
        }
    }
}

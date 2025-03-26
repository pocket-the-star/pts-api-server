package com.pts.api.user.application.service;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.common.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private static final String BEARER_PREFIX = "Bearer ";

    public TokenResponse generate(Long userId, UserRole userRole) {

        return new TokenResponse(
            create(userId, TokenType.ACCESS, userRole, ACCESS_TOKEN_EXPIRE_TIME),
            create(userId, TokenType.REFRESH, userRole, REFRESH_TOKEN_EXPIRE_TIME)
        );
    }

    private String create(Long userId, TokenType tokenType, UserRole userRole, Long expireTime) {
        return tokenProviderPort.create(userId, userRole, tokenType, expireTime);
    }

    public void parse(String bearerToken, String refreshToken) {
        String accessToken = extractToken(bearerToken);
        validateAccessToken(accessToken);
        validateRefreshToken(tokenProviderPort.getUserId(extractToken(bearerToken)),
            refreshToken);

    }

    public void saveRefreshToken(Long userId, String refreshToken) {
        refreshTokenRepositoryPort.save(userId, refreshToken);
    }

    public TokenType getTokenType(String bearerToken) {

        return tokenProviderPort.getTokenType(extractToken(bearerToken));
    }

    public Long getUserId(String bearerToken) {

        return tokenProviderPort.getUserId(extractToken(bearerToken));
    }

    public UserRole getUserRole(String bearerToken) {
        return tokenProviderPort.getUserRole(extractToken(bearerToken));
    }

    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new InvalidTokenException("유효하지 않은 토큰 형식입니다");
    }

    private void validateAccessToken(String accessToken) {
        TokenType tokenType = tokenProviderPort.getTokenType(accessToken);
        if (tokenType != TokenType.ACCESS) {
            throw new InvalidTokenException("Access Token이 아닙니다");
        }
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

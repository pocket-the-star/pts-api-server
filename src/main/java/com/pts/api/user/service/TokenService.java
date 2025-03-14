package com.pts.api.user.service;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.dto.response.TokenResponseDto;
import com.pts.api.user.exception.InvalidTokenException;
import com.pts.api.user.repository.RefreshTokenRepository;
import com.pts.api.user.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30;
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String BEARER_PREFIX = "Bearer ";

    public TokenResponseDto generate(Long userId, UserRole userRole) {

        return new TokenResponseDto(
            create(userId, TokenType.ACCESS, userRole, ACCESS_TOKEN_EXPIRE_TIME),
            create(userId, TokenType.REFRESH, userRole, REFRESH_TOKEN_EXPIRE_TIME)
        );
    }

    private String create(Long userId, TokenType tokenType, UserRole userRole, Long expireTime) {
        return tokenProvider.create(userId, userRole, tokenType, expireTime);
    }

    public void parse(String bearerToken, String refreshToken) {
        String accessToken = extractToken(bearerToken);
        validateAccessToken(accessToken);
        validateRefreshToken(tokenProvider.getUserId(bearerToken), refreshToken);

    }

    public void saveRefreshToken(Long userId, String refreshToken) {
        refreshTokenRepository.save(userId, refreshToken);
    }

    public TokenType getTokenType(String bearerToken) {
        return tokenProvider.getTokenType(extractToken(bearerToken));
    }

    public Long getUserId(String bearerToken) {
        return tokenProvider.getUserId(extractToken(bearerToken));
    }

    public UserRole getUserRole(String bearerToken) {
        return tokenProvider.getUserRole(extractToken(bearerToken));
    }

    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new InvalidTokenException("유효하지 않은 토큰 형식입니다");
    }

    private void validateAccessToken(String accessToken) {
        TokenType tokenType = tokenProvider.getTokenType(accessToken);
        if (tokenType != TokenType.ACCESS) {
            throw new InvalidTokenException("Access Token이 아닙니다");
        }
    }

    private void validateRefreshToken(Long userId, String refreshToken) {
        String storedRefreshToken = refreshTokenRepository.findOneById(userId)
            .orElseThrow(() -> new InvalidTokenException("저장된 Refresh Token이 없습니다"));

        if (!storedRefreshToken.equals(refreshToken)) {
            refreshTokenRepository.deleteById(userId);
            throw new InvalidTokenException("Refresh Token이 일치하지 않습니다");
        }
    }
}

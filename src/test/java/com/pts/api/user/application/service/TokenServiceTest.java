package com.pts.api.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.TokenProviderPort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("TokenService 클래스")
class TokenServiceTest extends BaseUnitTest {

    @Mock
    private TokenProviderPort tokenProviderPort;

    @Mock
    private RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    private TokenService tokenService;

    private static final Long TEST_USER_ID = 1L;
    private static final UserRole TEST_USER_ROLE = UserRole.USER;
    private static final String TEST_ACCESS_TOKEN = "test_access_token";
    private static final String TEST_REFRESH_TOKEN = "test_refresh_token";
    private static final String TEST_BEARER_TOKEN = "Bearer " + TEST_ACCESS_TOKEN;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(tokenProviderPort, refreshTokenRepositoryPort);
    }

    @Nested
    @DisplayName("generate 메서드 호출 시")
    class DescribeGenerate {

        @Test
        @DisplayName("액세스 토큰과 리프레시 토큰을 생성한다")
        void itCreatesTokens() {
            // given
            when(tokenProviderPort.create(eq(TEST_USER_ID), eq(TEST_USER_ROLE), eq(TokenType.ACCESS), anyLong()))
                .thenReturn(TEST_ACCESS_TOKEN);
            when(tokenProviderPort.create(eq(TEST_USER_ID), eq(TEST_USER_ROLE), eq(TokenType.REFRESH), anyLong()))
                .thenReturn(TEST_REFRESH_TOKEN);

            // when
            TokenResponse response = tokenService.generate(TEST_USER_ID, TEST_USER_ROLE);

            // then
            assertThat(response.accessToken()).isEqualTo(TEST_ACCESS_TOKEN);
            assertThat(response.refreshToken()).isEqualTo(TEST_REFRESH_TOKEN);
        }
    }

    @Nested
    @DisplayName("saveRefreshToken 메서드 호출 시")
    class DescribeSaveRefreshToken {

        @Test
        @DisplayName("리프레시 토큰을 저장한다")
        void itSavesToken() {
            // when
            tokenService.saveRefreshToken(TEST_USER_ID, TEST_REFRESH_TOKEN);

            // then
            verify(refreshTokenRepositoryPort).save(TEST_USER_ID, TEST_REFRESH_TOKEN);
        }
    }

    @Nested
    @DisplayName("getTokenType 메서드 호출 시")
    class DescribeGetTokenType {

        @Test
        @DisplayName("토큰 타입을 반환한다")
        void itReturnsTokenType() {
            // given
            when(tokenProviderPort.getTokenType(anyString())).thenReturn(TokenType.ACCESS);

            // when
            TokenType result = tokenService.getTokenType(TEST_BEARER_TOKEN);

            // then
            assertThat(result).isEqualTo(TokenType.ACCESS);
        }
    }

    @Nested
    @DisplayName("parse 메서드 호출 시")
    class DescribeParse {

        @Test
        @DisplayName("토큰을 검증한다")
        void itValidatesTokens() {
            // given
            when(tokenProviderPort.getTokenType(anyString())).thenReturn(TokenType.ACCESS);
            when(tokenProviderPort.getUserId(anyString())).thenReturn(TEST_USER_ID);
            when(refreshTokenRepositoryPort.findOneById(TEST_USER_ID))
                .thenReturn(Optional.of(TEST_REFRESH_TOKEN));

            // when & then
            tokenService.parse(TEST_BEARER_TOKEN, TEST_REFRESH_TOKEN);
        }
    }
} 
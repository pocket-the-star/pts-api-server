package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.application.service.ValidateTokenService;
import com.pts.api.user.common.exception.InvalidTokenException;
import com.pts.api.user.domain.model.TokenPayload;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidateTokenService 클래스")
class ValidateTokenServiceTest extends BaseUnitTest {

    @Mock
    private TokenProviderPort tokenProviderPort;
    @Mock
    private RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    private ValidateTokenService validateTokenService;

    private final String BEARER_PREFIX = "Bearer ";
    private final Long userId = 1L;
    private final String validAccessToken = "validAccess";
    private final String validRefreshToken = "validRefresh";
    private final String invalidBearer = "InvalidTokenFormat";

    private TokenPayload accessPayload;

    @BeforeEach
    void setUp() {
        validateTokenService = new ValidateTokenService(tokenProviderPort,
            refreshTokenRepositoryPort);
        accessPayload = TokenPayload.builder()
            .userId(userId)
            .tokenType(TokenType.ACCESS)
            .build();
    }

    @Nested
    @DisplayName("execute 메서드 호출 시")
    class DescribeExecute {

        @Nested
        @DisplayName("정상적인 경우")
        class ContextValid {

            @Test
            @DisplayName("TokenPayload를 반환한다")
            void itReturnsTokenPayload() {
                String bearerToken = BEARER_PREFIX + validAccessToken;
                when(tokenProviderPort.parseToken(validAccessToken)).thenReturn(accessPayload);
                when(refreshTokenRepositoryPort.findOneById(userId))
                    .thenReturn(Optional.of(validRefreshToken));

                TokenPayload result = validateTokenService.execute(bearerToken, validRefreshToken);

                assertThat(result).isEqualTo(accessPayload);
            }
        }

        @Nested
        @DisplayName("Bearer 토큰 형식이 유효하지 않으면")
        class ContextInvalidBearerFormat {

            @Test
            @DisplayName("InvalidTokenException을 발생시킨다")
            void itThrowsInvalidTokenException() {
                assertThatThrownBy(
                    () -> validateTokenService.execute(invalidBearer, validRefreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("유효하지 않은 토큰 형식입니다");
            }
        }

        @Nested
        @DisplayName("Access 토큰 타입이 ACCESS가 아니면")
        class ContextWrongTokenType {

            @Test
            @DisplayName("InvalidTokenException을 발생시킨다")
            void itThrowsInvalidTokenException() {
                String bearerToken = BEARER_PREFIX + validAccessToken;
                TokenPayload wrongPayload = TokenPayload.builder()
                    .userId(userId)
                    .tokenType(TokenType.REFRESH)
                    .build();
                when(tokenProviderPort.parseToken(validAccessToken)).thenReturn(wrongPayload);

                assertThatThrownBy(
                    () -> validateTokenService.execute(bearerToken, validRefreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Access Token이 아닙니다");
            }
        }

        @Nested
        @DisplayName("저장된 Refresh Token이 없으면")
        class ContextMissingRefreshToken {

            @Test
            @DisplayName("InvalidTokenException을 발생시킨다")
            void itThrowsInvalidTokenException() {
                String bearerToken = BEARER_PREFIX + validAccessToken;
                when(tokenProviderPort.parseToken(validAccessToken)).thenReturn(accessPayload);
                when(refreshTokenRepositoryPort.findOneById(userId)).thenReturn(Optional.empty());

                assertThatThrownBy(
                    () -> validateTokenService.execute(bearerToken, validRefreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("저장된 Refresh Token이 없습니다");
            }
        }

        @Nested
        @DisplayName("전달된 Refresh Token이 저장된 값과 일치하지 않으면")
        class ContextRefreshTokenMismatch {

            @Test
            @DisplayName("Refresh Token이 일치하지 않으면, 삭제 후 InvalidTokenException을 발생시킨다")
            void itThrowsInvalidTokenException() {
                String bearerToken = BEARER_PREFIX + validAccessToken;
                when(tokenProviderPort.parseToken(validAccessToken)).thenReturn(accessPayload);
                when(refreshTokenRepositoryPort.findOneById(userId))
                    .thenReturn(Optional.of("differentRefresh"));

                assertThatThrownBy(
                    () -> validateTokenService.execute(bearerToken, validRefreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Refresh Token이 일치하지 않습니다");

                verify(refreshTokenRepositoryPort).deleteById(userId);
            }
        }
    }
}

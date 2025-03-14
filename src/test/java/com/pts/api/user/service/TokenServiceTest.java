package com.pts.api.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.dto.response.TokenResponseDto;
import com.pts.api.user.exception.InvalidTokenException;
import com.pts.api.user.repository.RefreshTokenRepository;
import com.pts.api.user.security.TokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenService 클래스")
class TokenServiceTest extends BaseUnitTest {

    // Data
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    private TokenService tokenService;

    private static final Long TEST_USER_ID = 1L;
    private static final UserRole TEST_USER_ROLE = UserRole.NORMAL;
    private static final String TEST_ACCESS_TOKEN = "test.access.token";
    private static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private static final String TEST_BEARER_TOKEN = "Bearer " + TEST_ACCESS_TOKEN;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(tokenProvider, refreshTokenRepository);
    }

    @Nested
    @DisplayName("토큰을 생성할 때")
    class Generate {

        @Test
        @DisplayName("액세스 토큰과 리프레시 토큰을 생성한다")
        void generatesAccessAndRefreshTokens() {
            // Given
            when(tokenProvider.create(eq(TEST_USER_ID), eq(TEST_USER_ROLE), eq(TokenType.ACCESS),
                anyLong()))
                .thenReturn(TEST_ACCESS_TOKEN);
            when(tokenProvider.create(eq(TEST_USER_ID), eq(TEST_USER_ROLE), eq(TokenType.REFRESH),
                anyLong()))
                .thenReturn(TEST_REFRESH_TOKEN);

            // When
            TokenResponseDto result = tokenService.generate(TEST_USER_ID, TEST_USER_ROLE);

            // Then
            assertThat(result.accessToken()).isEqualTo(TEST_ACCESS_TOKEN);
            assertThat(result.refreshToken()).isEqualTo(TEST_REFRESH_TOKEN);
        }
    }

    @Nested
    @DisplayName("토큰을 파싱할 때")
    class Parse {

        @Nested
        @DisplayName("Bearer 접두사가 없는 토큰이라면")
        class WithoutBearerPrefix {

            @Test
            @DisplayName("유효하지 않은 토큰 예외가 발생한다")
            void throwsInvalidTokenException() {
                // Given
                String invalidToken = "invalid.token";

                // When & Then
                assertThatThrownBy(() -> tokenService.parse(invalidToken, TEST_REFRESH_TOKEN))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("유효하지 않은 토큰 형식입니다");
            }
        }

        @Nested
        @DisplayName("액세스 토큰이 아닌 경우")
        class WithNonAccessToken {

            @Test
            @DisplayName("유효하지 않은 토큰 예외가 발생한다")
            void throwsInvalidTokenException() {
                // Given
                when(tokenProvider.getTokenType(TEST_ACCESS_TOKEN)).thenReturn(TokenType.REFRESH);

                // When & Then
                assertThatThrownBy(() -> tokenService.parse(TEST_BEARER_TOKEN, TEST_REFRESH_TOKEN))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Access Token이 아닙니다");
            }
        }

        @Nested
        @DisplayName("저장된 리프레시 토큰이 없는 경우")
        class WithNoStoredRefreshToken {

            @Test
            @DisplayName("유효하지 않은 토큰 예외가 발생한다")
            void throwsInvalidTokenException() {
                // Given
                when(tokenProvider.getTokenType(TEST_ACCESS_TOKEN)).thenReturn(TokenType.ACCESS);
                when(tokenProvider.getUserId(TEST_BEARER_TOKEN)).thenReturn(TEST_USER_ID);
                when(refreshTokenRepository.findOneById(TEST_USER_ID)).thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> tokenService.parse(TEST_BEARER_TOKEN, TEST_REFRESH_TOKEN))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("저장된 Refresh Token이 없습니다");
            }
        }

        @Nested
        @DisplayName("리프레시 토큰이 일치하지 않는 경우")
        class WithMismatchedRefreshToken {

            @Test
            @DisplayName("토큰을 삭제하고 유효하지 않은 토큰 예외가 발생한다")
            void deletesTokenAndThrowsInvalidTokenException() {
                // Given
                when(tokenProvider.getTokenType(TEST_ACCESS_TOKEN)).thenReturn(TokenType.ACCESS);
                when(tokenProvider.getUserId(TEST_BEARER_TOKEN)).thenReturn(TEST_USER_ID);
                when(refreshTokenRepository.findOneById(TEST_USER_ID))
                    .thenReturn(Optional.of("different.refresh.token"));

                // When & Then
                assertThatThrownBy(() -> tokenService.parse(TEST_BEARER_TOKEN, TEST_REFRESH_TOKEN))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("Refresh Token이 일치하지 않습니다");
                verify(refreshTokenRepository).deleteById(TEST_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효한 토큰인 경우")
        class WithValidTokens {

            @Test
            @DisplayName("정상적으로 파싱된다")
            void parsesSuccessfully() {
                // Given
                when(tokenProvider.getTokenType(TEST_ACCESS_TOKEN)).thenReturn(TokenType.ACCESS);
                when(tokenProvider.getUserId(TEST_BEARER_TOKEN)).thenReturn(TEST_USER_ID);
                when(refreshTokenRepository.findOneById(TEST_USER_ID))
                    .thenReturn(Optional.of(TEST_REFRESH_TOKEN));

                // When & Then
                assertThatCode(() -> tokenService.parse(TEST_BEARER_TOKEN, TEST_REFRESH_TOKEN))
                    .doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("리프레시 토큰을 저장할 때")
    class SaveRefreshToken {

        @Test
        @DisplayName("리프레시 토큰이 저장소에 저장된다")
        void savesRefreshTokenToRepository() {
            // Given
            Long userId = TEST_USER_ID;
            String refreshToken = TEST_REFRESH_TOKEN;

            // When
            tokenService.saveRefreshToken(userId, refreshToken);

            // Then
            verify(refreshTokenRepository).save(userId, refreshToken);
        }
    }

    @Nested
    @DisplayName("토큰 정보를 조회할 때")
    class GetTokenInfo {

        @Test
        @DisplayName("토큰 타입을 반환한다")
        void returnsTokenType() {
            // Given
            when(tokenProvider.getTokenType(TEST_ACCESS_TOKEN)).thenReturn(TokenType.ACCESS);

            // When
            TokenType result = tokenService.getTokenType(TEST_BEARER_TOKEN);

            // Then
            assertThat(result).isEqualTo(TokenType.ACCESS);
        }

        @Test
        @DisplayName("사용자 ID를 반환한다")
        void returnsUserId() {
            // Given
            when(tokenProvider.getUserId(TEST_ACCESS_TOKEN)).thenReturn(TEST_USER_ID);

            // When
            Long result = tokenService.getUserId(TEST_BEARER_TOKEN);

            // Then
            assertThat(result).isEqualTo(TEST_USER_ID);
        }

        @Test
        @DisplayName("사용자 역할을 반환한다")
        void returnsUserRole() {
            // Given
            when(tokenProvider.getUserRole(TEST_ACCESS_TOKEN)).thenReturn(TEST_USER_ROLE);

            // When
            UserRole result = tokenService.getUserRole(TEST_BEARER_TOKEN);

            // Then
            assertThat(result).isEqualTo(TEST_USER_ROLE);
        }
    }
}

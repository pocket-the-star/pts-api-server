package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.request.SignInRequestDto;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.out.PasswordEncoderPort;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.application.service.SignInService;
import com.pts.api.user.application.service.internal.GenerateTokenService;
import com.pts.api.user.common.exception.PasswordNotMatchedException;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignInService 클래스")
class SignInServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private GenerateTokenService generateTokenService;
    @Mock
    private RefreshTokenRepositoryPort refreshTokenPort;

    private SignInService signInService;

    private final String email = "test@example.com";
    private final String rawPassword = "rawPass";
    private final String encodedPassword = "encodedPass";
    private final Long userId = 1L;
    private final String tokenAccess = "accessToken";
    private final String tokenRefresh = "refreshToken";

    @BeforeEach
    void setUp() {
        signInService = new SignInService(
            userRepositoryPort,
            passwordEncoderPort,
            generateTokenService,
            refreshTokenPort
        );
    }

    @Nested
    @DisplayName("execute 메서드 호출 시")
    class DescribeExecute {

        @Nested
        @DisplayName("사용자가 존재하지 않으면")
        class ContextUserNotFound {

            @Test
            @DisplayName("UserNotFoundException이 발생한다")
            void itThrowsUserNotFoundException() {
                // Given
                SignInRequestDto request = new SignInRequestDto(email, rawPassword);
                when(userRepositoryPort.findOneByEmail(email)).thenReturn(Optional.empty());

                // When, Then
                assertThatThrownBy(() -> signInService.execute(request))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("존재하지 않는 사용자입니다");
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않으면")
        class ContextPasswordMismatch {

            @Test
            @DisplayName("PasswordNotMatchedException이 발생한다")
            void itThrowsPasswordNotMatchedException() {
                // Given
                LocalAccount localAccount = LocalAccount.builder()
                    .email(email)
                    .password(encodedPassword)
                    .build();
                User user = User.builder()
                    .id(userId)
                    .localAccount(localAccount)
                    .build();
                SignInRequestDto request = new SignInRequestDto(email, rawPassword);

                when(userRepositoryPort.findOneByEmail(email)).thenReturn(Optional.of(user));
                when(passwordEncoderPort.matches(rawPassword, encodedPassword)).thenReturn(false);

                // When, Then
                assertThatThrownBy(() -> signInService.execute(request))
                    .isInstanceOf(PasswordNotMatchedException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다");
            }
        }

        @Nested
        @DisplayName("정상적인 경우")
        class ContextValid {

            @Test
            @DisplayName("TokenResponseDto를 반환하고 refreshToken 저장을 호출한다")
            void itReturnsTokenResponseDto() {
                // Given
                LocalAccount localAccount = LocalAccount.builder()
                    .email(email)
                    .password(encodedPassword)
                    .build();
                User user = User.builder()
                    .id(userId)
                    .localAccount(localAccount)
                    .role(UserRole.NORMAL)
                    .build();
                SignInRequestDto request = new SignInRequestDto(email, rawPassword);
                TokenResponseDto expectedResponse = new TokenResponseDto(tokenAccess, tokenRefresh);

                when(userRepositoryPort.findOneByEmail(email)).thenReturn(Optional.of(user));
                when(passwordEncoderPort.matches(rawPassword, encodedPassword)).thenReturn(true);
                when(generateTokenService.generate(userId, user.getRole())).thenReturn(
                    expectedResponse);

                // When
                TokenResponseDto actualResponse = signInService.execute(request);

                // Then
                assertThat(actualResponse).isEqualTo(expectedResponse);
                verify(refreshTokenPort).save(userId, tokenRefresh);
            }
        }
    }
}
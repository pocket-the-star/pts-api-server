package com.pts.api.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.domain.model.UserInfo;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("SignInApplicationService 클래스")
class SignInApplicationServiceTest extends BaseUnitTest {

    @Mock
    private EmailVerifyRepositoryPort emailVerifyRepository;

    @Mock
    private IDateTimeUtil dateTimeUtil;

    @Mock
    private TokenService tokenService;

    @Mock
    private IRandomUtil randomUtil;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private OutboxPublisher outboxPublisher;

    @Mock
    private UserRepositoryPort userRepository;

    private SignInApplicationService signInApplicationService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "테스트유저";
    private static final String TEST_NAME = "홍길동";
    private static final String TEST_PHONE = "01012345678";
    private static final String TEST_ADDRESS = "서울시 강남구";
    private static final String TEST_AUTH_CODE = "123456";
    private static final LocalDateTime TEST_NOW = LocalDateTime.now();
    private static final Long TEST_USER_ID = 1L;

    private User testUser;
    private EmailVerify testEmailVerify;

    @BeforeEach
    void setUp() {
        signInApplicationService = new SignInApplicationService(
            tokenService,
            authenticationService,
            userRepository
        );

        LocalAccount localAccount = LocalAccount.builder()
            .id(TEST_USER_ID)
            .email(TEST_EMAIL)
            .password(TEST_PASSWORD)
            .createdAt(TEST_NOW)
            .updatedAt(TEST_NOW)
            .build();

        UserInfo userInfo = UserInfo.builder()
            .id(TEST_USER_ID)
            .name(TEST_NAME)
            .phone(TEST_PHONE)
            .address(TEST_ADDRESS)
            .createdAt(TEST_NOW)
            .updatedAt(TEST_NOW)
            .build();

        testUser = User.builder()
            .id(TEST_USER_ID)
            .nickname(TEST_NICKNAME)
            .role(UserRole.USER)
            .localAccount(localAccount)
            .userInfo(userInfo)
            .createdAt(TEST_NOW)
            .updatedAt(TEST_NOW)
            .build();

        testEmailVerify = EmailVerify.builder()
            .email(TEST_EMAIL)
            .code(TEST_AUTH_CODE)
            .verified(true)
            .tryCount(0)
            .build();
    }

    @Nested
    @DisplayName("signIn 메서드 호출 시")
    class DescribeSignIn {

        @Test
        @DisplayName("유효한 요청이면 로그인에 성공하고 토큰을 반환한다")
        void signInSuccessfully() {
            // given
            SignInRequest request = new SignInRequest(TEST_EMAIL, TEST_PASSWORD);
            TokenResponse expectedToken = new TokenResponse("access_token", "refresh_token");

            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
            when(authenticationService.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
            when(tokenService.generate(TEST_USER_ID, UserRole.USER)).thenReturn(expectedToken);

            // when
            TokenResponse actualToken = signInApplicationService.signIn(request);

            // then
            assertThat(actualToken).isEqualTo(expectedToken);
            verify(tokenService).saveRefreshToken(TEST_USER_ID, expectedToken.refreshToken());
        }

        @Test
        @DisplayName("존재하지 않는 이메일이면 예외가 발생한다")
        void throwsExceptionWhenUserNotFound() {
            // given
            SignInRequest request = new SignInRequest(TEST_EMAIL, TEST_PASSWORD);

            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> signInApplicationService.signIn(request))
                .isInstanceOf(UserNotFoundException.class);
        }
    }
} 
package com.pts.api.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.PasswordMismatchException;
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

@DisplayName("UserApplicationService 클래스")
class UserApplicationServiceTest extends BaseUnitTest {

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

    private UserApplicationService userApplicationService;

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
        userApplicationService = new UserApplicationService(
            emailVerifyRepository,
            dateTimeUtil,
            tokenService,
            randomUtil,
            authenticationService,
            outboxPublisher,
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
    @DisplayName("signUp 메서드 호출 시")
    class DescribeSignUp {

        @Test
        @DisplayName("유효한 요청이면 회원가입에 성공한다")
        void signUpSuccessfully() {
            // given
            SignUpRequest request = new SignUpRequest(
                "USER",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
            );

            when(emailVerifyRepository.findByEmail(TEST_EMAIL)).thenReturn(
                Optional.of(testEmailVerify));
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(dateTimeUtil.now()).thenReturn(TEST_NOW);
            when(authenticationService.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // when
            userApplicationService.signUp(request);

            // then
            verify(userRepository).save(any(User.class));
            verify(emailVerifyRepository).deleteByEmail(TEST_EMAIL);
        }

        @Test
        @DisplayName("이메일 인증이 되지 않은 경우 예외가 발생한다")
        void throwsExceptionWhenEmailNotVerified() {
            // given
            SignUpRequest request = new SignUpRequest(
                "USER",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
            );

            when(emailVerifyRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userApplicationService.signUp(request))
                .isInstanceOf(EmailVerifyNotFoundException.class);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않으면 예외가 발생한다")
        void throwsExceptionWhenPasswordMismatch() {
            // given
            SignUpRequest request = new SignUpRequest(
                "USER",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                "different_password"
            );

            when(emailVerifyRepository.findByEmail(TEST_EMAIL)).thenReturn(
                Optional.of(testEmailVerify));

            // when & then
            assertThatThrownBy(() -> userApplicationService.signUp(request))
                .isInstanceOf(PasswordMismatchException.class);

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("이미 존재하는 이메일이면 예외가 발생한다")
        void throwsExceptionWhenEmailAlreadyExists() {
            // given
            SignUpRequest request = new SignUpRequest(
                "USER",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
            );

            when(emailVerifyRepository.findByEmail(TEST_EMAIL)).thenReturn(
                Optional.of(testEmailVerify));
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> userApplicationService.signUp(request))
                .isInstanceOf(AlreadyExistsException.class);

            verify(userRepository, never()).save(any(User.class));
        }
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
            TokenResponse actualToken = userApplicationService.signIn(request);

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
            assertThatThrownBy(() -> userApplicationService.signIn(request))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("verifyEmail 메서드 호출 시")
    class DescribeVerifyEmail {

        @Test
        @DisplayName("이메일 인증 요청에 성공한다")
        void verifyEmailSuccessfully() {
            // given
            when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(true);
            when(randomUtil.generateRandomString()).thenReturn(TEST_AUTH_CODE);
            doNothing().when(outboxPublisher)
                .publish(any(EventType.class), any(EmailVerifyData.class));

            // when
            userApplicationService.verifyEmail(TEST_EMAIL);

            // then
            verify(emailVerifyRepository).save(any(EmailVerify.class));
            verify(outboxPublisher).publish(EventType.EMAIL_AUTH,
                new EmailVerifyData(TEST_EMAIL, TEST_AUTH_CODE));
        }

        @Test
        @DisplayName("이메일 락 획득에 실패하면 예외가 발생한다")
        void throwsExceptionWhenLockFailed() {
            // given
            when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> userApplicationService.verifyEmail(TEST_EMAIL))
                .isInstanceOf(EmailVerifyLockedException.class);

            verify(emailVerifyRepository, never()).save(any(EmailVerify.class));
            verify(outboxPublisher, never()).publish(any(EventType.class),
                any(EmailVerifyData.class));
        }
    }
} 
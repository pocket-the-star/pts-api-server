package com.pts.api.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.PasswordMismatchException;
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
class SignUpApplicationServiceTest extends BaseUnitTest {

    @Mock
    private EmailVerifyRepositoryPort emailVerifyRepository;

    @Mock
    private IDateTimeUtil dateTimeUtil;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserRepositoryPort userRepository;

    private SignUpApplicationService signUpApplicationService;

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
        signUpApplicationService = new SignUpApplicationService(
            emailVerifyRepository,
            dateTimeUtil,
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
            signUpApplicationService.signUp(request);

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
            assertThatThrownBy(() -> signUpApplicationService.signUp(request))
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
            assertThatThrownBy(() -> signUpApplicationService.signUp(request))
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
            assertThatThrownBy(() -> signUpApplicationService.signUp(request))
                .isInstanceOf(AlreadyExistsException.class);

            verify(userRepository, never()).save(any(User.class));
        }
    }
} 
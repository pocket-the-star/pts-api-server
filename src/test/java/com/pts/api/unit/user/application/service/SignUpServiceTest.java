package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.user.application.dto.request.SignUpRequestDto;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.PasswordEncoderPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.application.service.SignUpService;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.PasswordMismatchException;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.domain.model.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignUpService 클래스")
class SignUpServiceTest extends BaseUnitTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private EmailVerifyRepositoryPort verificationRepositoryPort;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private IDateTimeUtil dateTimeUtil;

    private SignUpService signUpService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "pass123";
    private final String TEST_PASSWORD_CONFIRM = "pass123";
    private final String TEST_NICKNAME = "testUser";
    private final String TEST_USER_ROLE = "NORMAL";

    @BeforeEach
    void setUp() {
        signUpService = new SignUpService(userRepositoryPort, verificationRepositoryPort,
            passwordEncoderPort, dateTimeUtil);
    }

    @Nested
    @DisplayName("execute 메서드 호출 시")
    class DescribeExecute {

        @Nested
        @DisplayName("이메일 인증 정보가 존재하지 않으면")
        class ContextWhenEmailNotFound {

            @Test
            @DisplayName("EmailVerifyNotFoundException을 발생시킨다")
            void itThrowsEmailVerifyNotFoundException() {
                // Given
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(Optional.empty());
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    TEST_PASSWORD_CONFIRM);

                // When & Then
                assertThatThrownBy(() -> signUpService.execute(request)).isInstanceOf(
                    EmailVerifyNotFoundException.class).hasMessage("이메일 인증을 받지 않았습니다.");
            }
        }

        @Nested
        @DisplayName("이메일 인증은 있으나 인증되지 않았다면")
        class ContextWhenEmailNotVerified {

            @Test
            @DisplayName("InvalidCodeException을 발생시킨다")
            void itThrowsInvalidCodeException() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isVerified()).thenReturn(false);
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    TEST_PASSWORD_CONFIRM);

                // When & Then
                assertThatThrownBy(() -> signUpService.execute(request)).isInstanceOf(
                    InvalidCodeException.class).hasMessage("이메일 인증을 받지 않았습니다.");
            }
        }

        @Nested
        @DisplayName("패스워드와 패스워드 확인이 일치하지 않으면")
        class ContextWhenPasswordMismatch {

            @Test
            @DisplayName("PasswordMismatchException을 발생시킨다")
            void itThrowsPasswordMismatchException() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isVerified()).thenReturn(true);
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    "TEST_PASSWORD_CONFIRM");

                // When & Then
                assertThatThrownBy(() -> signUpService.execute(request)).isInstanceOf(
                    PasswordMismatchException.class).hasMessage("패스워드와 패스워드확인이 일치하지 않습니다.");
            }
        }

        @Nested
        @DisplayName("이미 존재하는 이메일이면")
        class ContextWhenEmailAlreadyExists {

            @Test
            @DisplayName("AlreadyExistsException을 발생시킨다")
            void itThrowsAlreadyExistsException() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isVerified()).thenReturn(true);
                when(userRepositoryPort.findOneByEmail(TEST_EMAIL)).thenReturn(
                    Optional.of(org.mockito.Mockito.mock(User.class)));
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    TEST_PASSWORD_CONFIRM);

                // When & Then
                assertThatThrownBy(() -> signUpService.execute(request)).isInstanceOf(
                    AlreadyExistsException.class).hasMessage("이미 존재하는 이메일입니다.");
            }
        }

        @Nested
        @DisplayName("모든 조건이 정상인 경우")
        class ContextWithValidRequest {

            @Test
            @DisplayName("UserRepositoryPort의 save 메서드가 호출된다")
            void itCallsUserRepositorySave() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isVerified()).thenReturn(true);
                when(userRepositoryPort.findOneByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
                when(passwordEncoderPort.encode(TEST_PASSWORD)).thenReturn("encodedPassword");
                LocalDateTime now = LocalDateTime.now();
                when(dateTimeUtil.now()).thenReturn(now);
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    TEST_PASSWORD_CONFIRM);

                when(userRepositoryPort.save(any(User.class))).thenReturn(
                    org.mockito.Mockito.mock(User.class));
                doNothing().when(verificationRepositoryPort).deleteById(TEST_EMAIL);

                // When
                signUpService.execute(request);

                // Then
                verify(userRepositoryPort).save(any(User.class));
            }

            @Test
            @DisplayName("verificationRepositoryPort의 deleteById가 호출된다")
            void itCallsVerificationRepositoryDeleteById() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(verificationRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isVerified()).thenReturn(true);
                when(userRepositoryPort.findOneByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
                when(passwordEncoderPort.encode(TEST_PASSWORD)).thenReturn("encodedPassword");
                LocalDateTime now = LocalDateTime.now();
                when(dateTimeUtil.now()).thenReturn(now);
                SignUpRequestDto request = new SignUpRequestDto(TEST_USER_ROLE, TEST_NICKNAME,
                    TEST_EMAIL, TEST_PASSWORD,
                    TEST_PASSWORD_CONFIRM);
                when(userRepositoryPort.save(any(User.class))).thenReturn(
                    org.mockito.Mockito.mock(User.class));
                doNothing().when(verificationRepositoryPort).deleteById(TEST_EMAIL);

                // When
                signUpService.execute(request);

                // Then
                verify(verificationRepositoryPort).deleteById(TEST_EMAIL);
            }
        }
    }
}


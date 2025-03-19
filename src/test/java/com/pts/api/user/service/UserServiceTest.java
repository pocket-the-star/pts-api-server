package com.pts.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.dto.request.SignInRequestDto;
import com.pts.api.user.dto.request.SignUpRequestDto;
import com.pts.api.user.dto.response.TokenResponseDto;
import com.pts.api.user.exception.AlreadyExistsException;
import com.pts.api.user.exception.EmailVerifyLockedException;
import com.pts.api.user.exception.EmailVerifyNotFoundException;
import com.pts.api.user.exception.InvalidCodeException;
import com.pts.api.user.exception.PasswordNotMatchedException;
import com.pts.api.user.exception.UserNotFoundException;
import com.pts.api.user.model.EmailVerify;
import com.pts.api.user.model.LocalAccount;
import com.pts.api.user.model.User;
import com.pts.api.user.repository.EmailVerifyRepository;
import com.pts.api.user.repository.UserRepository;
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
@DisplayName("UserService 클래스")
class UserServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailVerifyRepository emailVerifyRepository;
    @Mock
    private OutboxPublisher outboxPublisher;
    @Mock
    private IDateTimeUtil dateTimeUtil;
    @Mock
    private TokenService tokenService;
    @Mock
    private IRandomUtil randomUtil;
    @Mock
    private AuthenticationService authenticationService;

    private UserService userService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "testUser";
    private static final String TEST_AUTH_CODE = "123456";

    @BeforeEach
    void setUp() {
        userService = new UserService(
            userRepository,
            emailVerifyRepository,
            dateTimeUtil,
            tokenService,
            randomUtil,
            authenticationService,
            outboxPublisher
        );
    }

    @Nested
    @DisplayName("회원가입을 시도할 때")
    class SignUp {

        // Data
        private SignUpRequestDto request;

        @BeforeEach
        void setUp() {
            request = new SignUpRequestDto(
                "NORMAL",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
            );
        }

        @Nested
        @DisplayName("이메일이 인증되지 않은 상태라면")
        class WithUnverifiedEmail {

            @Test
            @DisplayName("이메일 인증 필요 예외가 발생한다")
            void throwsInvalidCodeException() {
                // Given
                EmailVerify emailVerify = EmailVerify.builder()
                    .email(TEST_EMAIL)
                    .verified(false)
                    .build();
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerify));

                // When & Then
                assertThatThrownBy(() -> userService.signUp(request))
                    .isInstanceOf(InvalidCodeException.class)
                    .hasMessage("이메일 인증을 받지 않았습니다.");
            }
        }

        @Nested
        @DisplayName("이미 존재하는 이메일이라면")
        class WithExistingEmail {

            @Test
            @DisplayName("이메일 중복 예외가 발생한다")
            void throwsAlreadyExistsException() {
                // Given
                EmailVerify emailVerify = EmailVerify.builder()
                    .email(TEST_EMAIL)
                    .verified(true)
                    .build();
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerify));
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(mock(User.class)));

                // When & Then
                assertThatThrownBy(() -> userService.signUp(request))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("이미 존재하는 이메일입니다.");
            }
        }

        @Nested
        @DisplayName("유효한 회원가입 요청이라면")
        class WithValidRequest {

            @Test
            @DisplayName("사용자를 저장하고 인증정보를 삭제한다")
            void savesUserAndDeletesVerification() {
                // Given
                EmailVerify emailVerify = EmailVerify.builder()
                    .email(TEST_EMAIL)
                    .verified(true)
                    .build();
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerify));
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.empty());
                when(dateTimeUtil.now())
                    .thenReturn(LocalDateTime.now());
                when(authenticationService.encode(TEST_PASSWORD))
                    .thenReturn("encodedPassword");

                // When
                userService.signUp(request);

                // Then
                verify(userRepository).save(any(User.class));
                verify(emailVerifyRepository).deleteById(TEST_EMAIL);
            }
        }
    }

    @Nested
    @DisplayName("로그인을 시도할 때")
    class SignIn {

        // Data
        private SignInRequestDto request;

        @BeforeEach
        void setUp() {
            request = new SignInRequestDto(TEST_EMAIL, TEST_PASSWORD);
        }

        @Nested
        @DisplayName("존재하지 않는 사용자라면")
        class WithNonExistentUser {

            @Test
            @DisplayName("사용자 없음 예외가 발생한다")
            void throwsUserNotFoundException() {
                // Given
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> userService.signIn(request))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("존재하지 않는 사용자입니다");
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않는다면")
        class WithIncorrectPassword {

            @Test
            @DisplayName("비밀번호 불일치 예외가 발생한다")
            void throwsPasswordNotMatchedException() {
                // Given
                User user = mock(User.class);
                LocalAccount localAccount = mock(LocalAccount.class);
                when(user.getLocalAccount()).thenReturn(localAccount);
                when(localAccount.getPassword()).thenReturn("encodedPassword");
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(user));
                when(authenticationService.matches(TEST_PASSWORD, "encodedPassword"))
                    .thenReturn(false);

                // When & Then
                assertThatThrownBy(() -> userService.signIn(request))
                    .isInstanceOf(PasswordNotMatchedException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다");
            }
        }

        @Nested
        @DisplayName("유효한 로그인 요청이라면")
        class WithValidCredentials {

            @Test
            @DisplayName("토큰을 생성하고 리프레시 토큰을 저장한다")
            void generatesAndSavesTokens() {
                // Given
                User user = mock(User.class);
                LocalAccount localAccount = mock(LocalAccount.class);
                when(user.getLocalAccount()).thenReturn(localAccount);
                when(localAccount.getPassword()).thenReturn("encodedPassword");
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(user));
                when(authenticationService.matches(TEST_PASSWORD, "encodedPassword"))
                    .thenReturn(true);

                TokenResponseDto expectedToken = new TokenResponseDto("accessToken",
                    "refreshToken");
                when(tokenService.generate(any(), any())).thenReturn(expectedToken);

                // When
                TokenResponseDto actualToken = userService.signIn(request);

                // Then
                verify(tokenService).generate(any(), any());
                verify(tokenService).saveRefreshToken(any(), eq("refreshToken"));
                assertThat(actualToken).isEqualTo(expectedToken);
            }
        }
    }

    @Nested
    @DisplayName("이메일 인증을 요청할 때")
    class VerifyEmail {

        @Nested
        @DisplayName("락 획득에 실패한다면")
        class WithFailedLockAcquisition {

            @Test
            @DisplayName("락 획득 실패 예외가 발생한다")
            void throwsEmailVerifyLockedException() {
                // Given
                when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(false);

                // When & Then
                assertThatThrownBy(() -> userService.verifyEmail(TEST_EMAIL))
                    .isInstanceOf(EmailVerifyLockedException.class)
                    .hasMessage("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
            }
        }

        @Nested
        @DisplayName("유효한 인증 요청이라면")
        class WithValidRequest {

            @Test
            @DisplayName("인증 정보를 저장하고 이벤트를 발행한다")
            void savesVerificationAndPublishesEvent() {
                // Given
                when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(true);
                when(randomUtil.generateRandomString()).thenReturn(TEST_AUTH_CODE);

                // When
                userService.verifyEmail(TEST_EMAIL);

                // Then
                verify(outboxPublisher).publish(
                    EventType.EMAIL_AUTH,
                    new EmailVerifyData(TEST_EMAIL, TEST_AUTH_CODE)
                );
                verify(emailVerifyRepository).save(any(EmailVerify.class));
                verify(emailVerifyRepository).releaseLock(TEST_EMAIL);
            }
        }
    }

    @Nested
    @DisplayName("인증 코드를 확인할 때")
    class Confirm {

        // Data
        private AuthCodeConfirmRequestDto request;

        @BeforeEach
        void setUp() {
            request = new AuthCodeConfirmRequestDto(TEST_EMAIL, TEST_AUTH_CODE);
        }

        @Nested
        @DisplayName("인증 정보가 존재하지 않는다면")
        class WithNonExistentVerification {

            @Test
            @DisplayName("인증 정보 없음 예외가 발생한다")
            void throwsEmailVerifyNotFoundException() {
                // Given
                when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> userService.confirm(request))
                    .isInstanceOf(EmailVerifyNotFoundException.class)
                    .hasMessage("이메일 인증 정보를 찾을 수 없습니다.");
                verify(emailVerifyRepository).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("인증 시도 횟수를 초과했다면")
        class WithExceededTryCount {

            @Test
            @DisplayName("시도 횟수 초과 예외가 발생하고 인증 정보가 삭제된다")
            void throwsInvalidCodeExceptionAndDeletesVerification() {
                // Given
                when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(true);
                EmailVerify emailVerify = EmailVerify.builder()
                    .email(TEST_EMAIL)
                    .tryCount(5)
                    .build();
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerify));

                // When & Then
                assertThatThrownBy(() -> userService.confirm(request))
                    .isInstanceOf(InvalidCodeException.class)
                    .hasMessage("인증 시도 횟수를 초과했습니다.");
                verify(emailVerifyRepository).deleteById(TEST_EMAIL);
                verify(emailVerifyRepository).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("유효한 인증 코드라면")
        class WithValidAuthCode {

            @Test
            @DisplayName("인증 상태를 업데이트한다")
            void updatesVerificationStatus() {
                // Given
                when(emailVerifyRepository.getLock(TEST_EMAIL)).thenReturn(true);
                EmailVerify emailVerify = EmailVerify.builder()
                    .email(TEST_EMAIL)
                    .authCode(TEST_AUTH_CODE)
                    .tryCount(0)
                    .verified(false)
                    .build();
                when(emailVerifyRepository.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerify));

                // When
                userService.confirm(request);

                // Then
                verify(emailVerifyRepository).save(any(EmailVerify.class));
                verify(emailVerifyRepository).releaseLock(TEST_EMAIL);
            }
        }
    }
}

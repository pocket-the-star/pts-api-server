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
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.service.AuthenticationService;
import com.pts.api.user.application.service.TokenService;
import com.pts.api.user.application.service.UserApplicationService;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.PasswordNotMatchedException;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.infrastructure.cache.adapter.EmailVerifyRepositoryAdapter;
import com.pts.api.user.infrastructure.cache.model.EmailVerifyEntity;
import com.pts.api.user.infrastructure.persistence.entity.LocalAccountEntity;
import com.pts.api.user.infrastructure.persistence.entity.UserEntity;
import com.pts.api.user.infrastructure.persistence.repository.UserRepository;
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
class UserEntityApplicationServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailVerifyRepositoryAdapter emailVerifyRepositoryAdapter;
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

    private UserApplicationService userApplicationService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "testUser";
    private static final String TEST_AUTH_CODE = "123456";

    @BeforeEach
    void setUp() {
        userApplicationService = new UserApplicationService(
            userRepository,
            emailVerifyRepositoryAdapter,
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
        private SignUpRequest request;

        @BeforeEach
        void setUp() {
            request = new SignUpRequest(
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
                EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.builder()
                    .email(TEST_EMAIL)
                    .verified(false)
                    .build();
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerifyEntity));

                // When & Then
                assertThatThrownBy(() -> userApplicationService.signUp(request))
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
                EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.builder()
                    .email(TEST_EMAIL)
                    .verified(true)
                    .build();
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerifyEntity));
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(mock(UserEntity.class)));

                // When & Then
                assertThatThrownBy(() -> userApplicationService.signUp(request))
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
                EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.builder()
                    .email(TEST_EMAIL)
                    .verified(true)
                    .build();
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerifyEntity));
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.empty());
                when(dateTimeUtil.now())
                    .thenReturn(LocalDateTime.now());
                when(authenticationService.encode(TEST_PASSWORD))
                    .thenReturn("encodedPassword");

                // When
                userApplicationService.signUp(request);

                // Then
                verify(userRepository).save(any(UserEntity.class));
                verify(emailVerifyRepositoryAdapter).deleteById(TEST_EMAIL);
            }
        }
    }

    @Nested
    @DisplayName("로그인을 시도할 때")
    class SignIn {

        // Data
        private SignInRequest request;

        @BeforeEach
        void setUp() {
            request = new SignInRequest(TEST_EMAIL, TEST_PASSWORD);
        }

        @Nested
        @DisplayName("존재하지 않는 사용자라면")
        class WithNonExistentUserEntity {

            @Test
            @DisplayName("사용자 없음 예외가 발생한다")
            void throwsUserNotFoundException() {
                // Given
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> userApplicationService.signIn(request))
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
                UserEntity userEntity = mock(UserEntity.class);
                LocalAccountEntity localAccountEntity = mock(LocalAccountEntity.class);
                when(userEntity.getLocalAccountEntity()).thenReturn(localAccountEntity);
                when(localAccountEntity.getPassword()).thenReturn("encodedPassword");
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(userEntity));
                when(authenticationService.matches(TEST_PASSWORD, "encodedPassword"))
                    .thenReturn(false);

                // When & Then
                assertThatThrownBy(() -> userApplicationService.signIn(request))
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
                UserEntity userEntity = mock(UserEntity.class);
                LocalAccountEntity localAccountEntity = mock(LocalAccountEntity.class);
                when(userEntity.getLocalAccountEntity()).thenReturn(localAccountEntity);
                when(localAccountEntity.getPassword()).thenReturn("encodedPassword");
                when(userRepository.findOneByEmail(TEST_EMAIL))
                    .thenReturn(Optional.of(userEntity));
                when(authenticationService.matches(TEST_PASSWORD, "encodedPassword"))
                    .thenReturn(true);

                TokenResponse expectedToken = new TokenResponse("accessToken",
                    "refreshToken");
                when(tokenService.generate(any(), any())).thenReturn(expectedToken);

                // When
                TokenResponse actualToken = userApplicationService.signIn(request);

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
                when(emailVerifyRepositoryAdapter.getLock(TEST_EMAIL)).thenReturn(false);

                // When & Then
                assertThatThrownBy(() -> userApplicationService.verifyEmail(TEST_EMAIL))
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
                when(emailVerifyRepositoryAdapter.getLock(TEST_EMAIL)).thenReturn(true);
                when(randomUtil.generateRandomString()).thenReturn(TEST_AUTH_CODE);

                // When
                userApplicationService.verifyEmail(TEST_EMAIL);

                // Then
                verify(outboxPublisher).publish(
                    EventType.EMAIL_AUTH,
                    new EmailVerifyData(TEST_EMAIL, TEST_AUTH_CODE)
                );
                verify(emailVerifyRepositoryAdapter).save(any(EmailVerifyEntity.class));
                verify(emailVerifyRepositoryAdapter).releaseLock(TEST_EMAIL);
            }
        }
    }

    @Nested
    @DisplayName("인증 코드를 확인할 때")
    class Confirm {

        // Data
        private AuthCodeConfirmRequest request;

        @BeforeEach
        void setUp() {
            request = new AuthCodeConfirmRequest(TEST_EMAIL, TEST_AUTH_CODE);
        }

        @Nested
        @DisplayName("인증 정보가 존재하지 않는다면")
        class WithNonExistentVerification {

            @Test
            @DisplayName("인증 정보 없음 예외가 발생한다")
            void throwsEmailVerifyNotFoundException() {
                // Given
                when(emailVerifyRepositoryAdapter.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> userApplicationService.confirm(request))
                    .isInstanceOf(EmailVerifyNotFoundException.class)
                    .hasMessage("이메일 인증 정보를 찾을 수 없습니다.");
                verify(emailVerifyRepositoryAdapter).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("인증 시도 횟수를 초과했다면")
        class WithExceededTryCount {

            @Test
            @DisplayName("시도 횟수 초과 예외가 발생하고 인증 정보가 삭제된다")
            void throwsInvalidCodeExceptionAndDeletesVerification() {
                // Given
                when(emailVerifyRepositoryAdapter.getLock(TEST_EMAIL)).thenReturn(true);
                EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.builder()
                    .email(TEST_EMAIL)
                    .tryCount(5)
                    .build();
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerifyEntity));

                // When & Then
                assertThatThrownBy(() -> userApplicationService.confirm(request))
                    .isInstanceOf(InvalidCodeException.class)
                    .hasMessage("인증 시도 횟수를 초과했습니다.");
                verify(emailVerifyRepositoryAdapter).deleteById(TEST_EMAIL);
                verify(emailVerifyRepositoryAdapter).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("유효한 인증 코드라면")
        class WithValidAuthCode {

            @Test
            @DisplayName("인증 상태를 업데이트한다")
            void updatesVerificationStatus() {
                // Given
                when(emailVerifyRepositoryAdapter.getLock(TEST_EMAIL)).thenReturn(true);
                EmailVerifyEntity emailVerifyEntity = EmailVerifyEntity.builder()
                    .email(TEST_EMAIL)
                    .authCode(TEST_AUTH_CODE)
                    .tryCount(0)
                    .verified(false)
                    .build();
                when(emailVerifyRepositoryAdapter.findById(TEST_EMAIL))
                    .thenReturn(Optional.of(emailVerifyEntity));

                // When
                userApplicationService.confirm(request);

                // Then
                verify(emailVerifyRepositoryAdapter).save(any(EmailVerifyEntity.class));
                verify(emailVerifyRepositoryAdapter).releaseLock(TEST_EMAIL);
            }
        }
    }
}

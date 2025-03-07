package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.port.out.EmailVerifyProducerPort;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.service.EmailVerifyService;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.domain.model.EmailVerify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerifyService 클래스")
class EmailVerifyServiceTest extends BaseUnitTest {

    @Mock
    private EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    @Mock
    private EmailVerifyProducerPort emailVerifyProducerPort;
    @Mock
    private IRandomUtil randomUtil;

    private EmailVerifyService emailVerifyService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_AUTH_CODE = "abc123";

    @BeforeEach
    void setUp() {
        emailVerifyService = new EmailVerifyService(
            emailVerifyRepositoryPort,
            emailVerifyProducerPort,
            randomUtil
        );
    }

    @Nested
    @DisplayName("verifyEmail 메서드 실행 시")
    class Describe_verifyEmail {

        @Nested
        @DisplayName("락 획득에 실패하면")
        class Context_when_lock_not_acquired {

            @Test
            @DisplayName("EmailVerifyLockedException을 발생시킨다")
            void it_throws_locked_exception() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(false);

                // When || Then
                assertThatThrownBy(
                    () -> emailVerifyService.verifyEmail(TEST_EMAIL))
                    .isInstanceOf(EmailVerifyLockedException.class)
                    .hasMessage("이메일 인증을 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }
        }

        @Nested
        @DisplayName("정상적인 요청이 주어지면")
        class Context_when_valid_request {

            @BeforeEach
            void setUp() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(randomUtil.generateRandomString()).thenReturn(TEST_AUTH_CODE);
            }

            @Test
            @DisplayName("인증 코드를 생성한다")
            void it_generates_auth_code() {
                // When
                emailVerifyService.verifyEmail(TEST_EMAIL);

                // Then: 인증코드 생성을 위해 randomUtil.generateRandomString() 호출 검증
                verify(randomUtil).generateRandomString();
            }

            @Test
            @DisplayName("이메일 인증 이벤트를 발행한다")
            void it_publishes_email_verification_event() {
                // When
                emailVerifyService.verifyEmail(TEST_EMAIL);

                // Then: 이메일 인증 이벤트 발행 호출 검증
                verify(emailVerifyProducerPort).emailVerify(TEST_EMAIL, TEST_AUTH_CODE);
            }

            @Test
            @DisplayName("인증 정보를 저장한다")
            void it_saves_email_verify_info() {
                // When
                emailVerifyService.verifyEmail(TEST_EMAIL);

                // Then: 인증 정보를 저장하는 호출 검증
                verify(emailVerifyRepositoryPort).save(any(EmailVerify.class));
            }

            @Test
            @DisplayName("락을 해제한다")
            void it_releases_lock() {
                // When
                emailVerifyService.verifyEmail(TEST_EMAIL);

                // Then: 락 해제 호출 검증
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("이메일 인증 이벤트 발행 중 예외가 발생하면")
        class Context_when_event_publication_fails {

            @Test
            @DisplayName("예외를 전파하고 락을 해제한다")
            void it_propagates_exception_and_releases_lock() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(randomUtil.generateRandomString()).thenReturn(TEST_AUTH_CODE);
                doThrow(new RuntimeException("이메일 인증 이벤트 발행 실패"))
                    .when(emailVerifyProducerPort).emailVerify(TEST_EMAIL, TEST_AUTH_CODE);

                // When
                assertThatThrownBy(
                    () -> emailVerifyService.verifyEmail(TEST_EMAIL))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("이메일 인증 이벤트 발행 실패");

                // Then
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }
    }
}


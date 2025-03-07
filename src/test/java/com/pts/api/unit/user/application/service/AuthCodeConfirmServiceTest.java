package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.service.AuthCodeConfirmService;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.TryCountExceededException;
import com.pts.api.user.domain.model.EmailVerify;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("AuthCodeConfirmService 클래스")
class AuthCodeConfirmServiceTest extends BaseUnitTest {

    @Mock
    private EmailVerifyRepositoryPort emailVerifyRepositoryPort;

    private AuthCodeConfirmService authCodeConfirmService;

    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_AUTH_CODE = "abc123";

    @BeforeEach
    void setUp() {
        authCodeConfirmService = new AuthCodeConfirmService(emailVerifyRepositoryPort);
    }

    @Nested
    @DisplayName("confirm 메서드 호출 시")
    class DescribeConfirm {

        @Nested
        @DisplayName("락 획득에 실패하면")
        class ContextWhenLockNotAcquired {

            @Test
            @DisplayName("예외를 발생시킨다 (EmailVerifyLockedException)")
            void itThrowsLockedException() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(false);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);
                // When & Then
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    EmailVerifyLockedException.class).hasMessage("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
            }

            @Test
            @DisplayName("getLock이 호출되었음을 검증한다")
            void itCallsGetLock() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(false);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    EmailVerifyLockedException.class).hasMessage("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");

                // Then
                verify(emailVerifyRepositoryPort).getLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("인증 정보가 존재하지 않으면")
        class ContextWhenEmailNotFound {

            @Test
            @DisplayName("예외를 발생시킨다 (EmailVerifyNotFoundException)")
            void itThrowsNotFoundException() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(Optional.empty());
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);
                // When & Then
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    EmailVerifyNotFoundException.class).hasMessage("존재하지 않는 인증 이메일입니다. 다시 인증해주세요.");
            }

            @Test
            @DisplayName("락 해제가 호출되었음을 검증한다")
            void itCallsReleaseLock() {
                // Given
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(Optional.empty());
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    EmailVerifyNotFoundException.class).hasMessage("존재하지 않는 인증 이메일입니다. 다시 인증해주세요.");

                // Then
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("시도 횟수를 초과했으면")
        class ContextWhenTryCountExceeded {

            @Test
            @DisplayName("예외를 발생시킨다 (TryCountExceededException)")
            void itThrowsTryCountExceededException() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(true);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When & Then
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                        TryCountExceededException.class)
                    .hasMessage("인증 시도 횟수를 초과하였습니다. 잠시 후 다시 시도해주세요.");
            }

            @Test
            @DisplayName("락 해제가 호출되었음을 검증한다")
            void itCallsReleaseLock() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(true);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                        TryCountExceededException.class)
                    .hasMessage("인증 시도 횟수를 초과하였습니다. 잠시 후 다시 시도해주세요.");

                // Then
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("인증번호가 일치하지 않으면")
        class ContextWhenInvalidAuthCode {

            @Test
            @DisplayName("예외를 발생시킨다 (InvalidCodeException)")
            void itThrowsInvalidCodeException() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(false);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When & Then
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    InvalidCodeException.class).hasMessage("인증번호가 일치하지 않습니다. 다시 확인해주세요.");
            }

            @Test
            @DisplayName("실패 시도 저장이 호출되었음을 검증한다")
            void itCallsSaveForFailedAttempt() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(false);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    InvalidCodeException.class).hasMessage("인증번호가 일치하지 않습니다. 다시 확인해주세요.");

                // Then
                verify(emailVerifyRepositoryPort).save(emailVerify);
            }

            @Test
            @DisplayName("락 해제가 호출되었음을 검증한다")
            void itCallsReleaseLock() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(false);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                assertThatThrownBy(() -> authCodeConfirmService.confirm(request)).isInstanceOf(
                    InvalidCodeException.class).hasMessage("인증번호가 일치하지 않습니다. 다시 확인해주세요.");

                // Then
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("정상적인 요청이면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("인증 객체의 verify 메서드가 호출되었음을 검증한다")
            void itCallsVerify() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(true);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);
                // When
                authCodeConfirmService.confirm(request);
                // Then
                verify(emailVerify).verify();
            }

            @Test
            @DisplayName("인증 객체가 저장되었음을 검증한다")
            void itCallsSave() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(true);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);
                doNothing().when(emailVerifyRepositoryPort).save(any(EmailVerify.class));

                // When
                authCodeConfirmService.confirm(request);

                // Then
                verify(emailVerifyRepositoryPort).save(emailVerify);
            }

            @Test
            @DisplayName("락 해제가 호출되었음을 검증한다")
            void itCallsReleaseLock() {
                // Given
                EmailVerify emailVerify = org.mockito.Mockito.mock(EmailVerify.class);
                when(emailVerifyRepositoryPort.getLock(TEST_EMAIL)).thenReturn(true);
                when(emailVerifyRepositoryPort.findById(TEST_EMAIL)).thenReturn(
                    Optional.of(emailVerify));
                when(emailVerify.isOverTryCount()).thenReturn(false);
                when(emailVerify.isMatchAuthCode(TEST_AUTH_CODE)).thenReturn(true);
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(TEST_EMAIL,
                    TEST_AUTH_CODE);

                // When
                authCodeConfirmService.confirm(request);

                // Then
                verify(emailVerifyRepositoryPort).releaseLock(TEST_EMAIL);
            }
        }
    }
}

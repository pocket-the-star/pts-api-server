package com.pts.api.user.application.service;

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
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.domain.model.UserInfo;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("VerifyEmailApplicationService 클래스")
class VerifyEmailApplicationServiceTest extends BaseUnitTest {

    @Mock
    private EmailVerifyRepositoryPort emailVerifyRepository;

    @Mock
    private IRandomUtil randomUtil;

    @Mock
    private OutboxPublisher outboxPublisher;

    private VerifyEmailApplicationService verifyEmailApplicationService;

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
        verifyEmailApplicationService = new VerifyEmailApplicationService(
            emailVerifyRepository,
            randomUtil,
            outboxPublisher
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
            verifyEmailApplicationService.verifyEmail(TEST_EMAIL);

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
            assertThatThrownBy(() -> verifyEmailApplicationService.verifyEmail(TEST_EMAIL))
                .isInstanceOf(EmailVerifyLockedException.class);

            verify(emailVerifyRepository, never()).save(any(EmailVerify.class));
            verify(outboxPublisher, never()).publish(any(EventType.class),
                any(EmailVerifyData.class));
        }
    }
} 
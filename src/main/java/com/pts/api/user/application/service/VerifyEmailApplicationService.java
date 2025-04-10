package com.pts.api.user.application.service;

import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.port.in.VerifyEmailUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.domain.model.EmailVerify;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailApplicationService implements VerifyEmailUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    private final IRandomUtil randomUtil;
    private final OutboxPublisher outboxPublisher;

    /**
     * 이메일 인증 확인
     *
     * @param email 이메일 인증 요청
     */
    @Override
    @Transactional
    public void verifyEmail(String email) {
        acquireLockOrThrow(email);

        try {
            String authCode = generateAuthCode(email);
            publishEmailVerificationEvent(email, authCode);
            saveEmailVerify(email, authCode);
        } finally {
            releaseLock(email);
        }
    }

    /**
     * 이메일 인증 확인
     *
     * @param email    이메일
     * @param authCode 인증 코드
     */
    private void publishEmailVerificationEvent(String email, String authCode) {
        outboxPublisher.publish(
            EventType.EMAIL_AUTH,
            new EmailVerifyData(email, authCode)
        );
    }

    private void saveEmailVerify(String email, String authCode) {
        int INIT_TRY_COUNT = 0;

        EmailVerify emailVerify = EmailVerify.builder()
            .email(email)
            .code(authCode)
            .tryCount(INIT_TRY_COUNT)
            .verified(false)
            .build();

        emailVerifyRepositoryPort.save(emailVerify);
    }

    private void acquireLockOrThrow(String email) {
        if (!emailVerifyRepositoryPort.getLock(email)) {
            throw new EmailVerifyLockedException("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private void releaseLock(String email) {
        emailVerifyRepositoryPort.releaseLock(email);
    }

    private String generateAuthCode(String email) {
        return randomUtil.generateRandomString();
    }
}
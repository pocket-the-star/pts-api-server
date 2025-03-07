package com.pts.api.user.application.service;

import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.port.in.EmailVerifyUseCase;
import com.pts.api.user.application.port.out.EmailVerifyProducerPort;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.domain.model.EmailVerify;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerifyService implements EmailVerifyUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    private final EmailVerifyProducerPort emailVerifyProducerPort;
    private final IRandomUtil randomUtil;

    @Override
    @Transactional
    public void verifyEmail(String email) {
        acquireLock(email);
        try {
            String authCode = generateAuthCode(email);
            publishEmailVerificationEvent(email, authCode);
            saveEmailVerify(email, authCode);
        } finally {
            releaseLock(email);
        }
    }

    private void acquireLock(String email) {
        if (!emailVerifyRepositoryPort.getLock(email)) {
            throw new EmailVerifyLockedException("이메일 인증을 처리 중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private String generateAuthCode(String email) {

        return randomUtil.generateRandomString();
    }

    private void publishEmailVerificationEvent(String email, String authCode) {
        emailVerifyProducerPort.emailVerify(email, authCode);
    }

    private void saveEmailVerify(String email, String authCode) {
        int INIT_TRY_COUNT = 0;

        EmailVerify emailVerify = EmailVerify.builder()
            .email(email)
            .authCode(authCode)
            .tryCount(INIT_TRY_COUNT)
            .isVerified(false)
            .build();

        emailVerifyRepositoryPort.save(emailVerify);
    }

    private void releaseLock(String email) {
        emailVerifyRepositoryPort.releaseLock(email);
    }
}

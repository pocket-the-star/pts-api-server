package com.pts.api.user.application.service;

import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;
import com.pts.api.user.application.port.in.ConfirmEmailUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.domain.model.EmailVerify;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmEmailApplicationService implements ConfirmEmailUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;

    /**
     * 이메일 인증 확인
     *
     * @param request 이메일 인증 요청
     */
    @Override
    @Transactional
    public void confirm(AuthCodeConfirmRequest request) {
        acquireLockOrThrow(request.email());
        try {
            EmailVerify emailVerify = emailVerifyRepositoryPort.findByEmail(request.email())
                .orElseThrow(() -> new EmailVerifyNotFoundException("이메일 인증 정보를 찾을 수 없습니다."));

            if (emailVerify.isOverTryCount()) {
                emailVerifyRepositoryPort.deleteByEmail(request.email());
                throw new InvalidCodeException("인증 시도 횟수를 초과했습니다.");
            }

            if (!emailVerify.getCode().equals(request.authCode())) {
                emailVerify.incrementTryCount();
                emailVerifyRepositoryPort.save(emailVerify);
                throw new InvalidCodeException("인증 코드가 일치하지 않습니다.");
            }

            emailVerify.verifyTrue();
            emailVerifyRepositoryPort.save(emailVerify);
        } finally {
            releaseLock(request.email());
        }
    }

    private void acquireLockOrThrow(String email) {
        if (!emailVerifyRepositoryPort.getLock(email)) {
            throw new EmailVerifyLockedException("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private void releaseLock(String email) {
        emailVerifyRepositoryPort.releaseLock(email);
    }
}
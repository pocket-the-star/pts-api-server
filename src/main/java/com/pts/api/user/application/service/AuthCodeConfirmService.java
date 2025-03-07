package com.pts.api.user.application.service;

import com.pts.api.user.application.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.application.port.in.AuthCodeConfirmUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.TryCountExceededException;
import com.pts.api.user.domain.model.EmailVerify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCodeConfirmService implements AuthCodeConfirmUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;

    @Override
    public void confirm(AuthCodeConfirmRequestDto request) {
        acquireLockOrThrow(request.email());
        try {
            EmailVerify emailAuth = getEmailVerifyOrThrow(request.email());
            validateTryCount(emailAuth);
            validateAuthCode(emailAuth, request.authCode());
            processSuccessfulVerification(emailAuth);
        } finally {
            releaseLock(request.email());
        }
    }

    private void acquireLockOrThrow(String email) {
        if (!emailVerifyRepositoryPort.getLock(email)) {
            throw new EmailVerifyLockedException("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private EmailVerify getEmailVerifyOrThrow(String email) {
        return emailVerifyRepositoryPort.findById(email)
            .orElseThrow(() -> new EmailVerifyNotFoundException("존재하지 않는 인증 이메일입니다. 다시 인증해주세요."));
    }

    private void validateTryCount(EmailVerify emailAuth) {
        if (emailAuth.isOverTryCount()) {
            throw new TryCountExceededException("인증 시도 횟수를 초과하였습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private void validateAuthCode(EmailVerify emailAuth, String authCode) {
        if (!emailAuth.isMatchAuthCode(authCode)) {
            processFailedAttempt(emailAuth);
            throw new InvalidCodeException("인증번호가 일치하지 않습니다. 다시 확인해주세요.");
        }
    }

    private void processFailedAttempt(EmailVerify emailAuth) {
        emailAuth.updateTryCount();
        emailVerifyRepositoryPort.save(emailAuth);
    }

    private void processSuccessfulVerification(EmailVerify emailAuth) {
        emailAuth.verify();
        emailVerifyRepositoryPort.save(emailAuth);
    }

    private void releaseLock(String email) {
        emailVerifyRepositoryPort.releaseLock(email);
    }

}

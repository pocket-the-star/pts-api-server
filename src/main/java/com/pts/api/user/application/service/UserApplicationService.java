package com.pts.api.user.application.service;

import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.EmailVerifyData;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.lib.internal.shared.util.random.IRandomUtil;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.in.ConfirmEmailUseCase;
import com.pts.api.user.application.port.in.SignInUseCase;
import com.pts.api.user.application.port.in.SignUpUseCase;
import com.pts.api.user.application.port.in.VerifyEmailUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyLockedException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.PasswordMismatchException;
import com.pts.api.user.common.exception.PasswordNotMatchedException;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.domain.model.UserInfo;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService implements SignUpUseCase, SignInUseCase, VerifyEmailUseCase,
    ConfirmEmailUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    private final IDateTimeUtil dateTimeUtil;
    private final TokenService tokenService;
    private final IRandomUtil randomUtil;
    private final AuthenticationService authenticationService;
    private final OutboxPublisher outboxPublisher;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        isEmailVerified(request.email());
        passwordCheck(request.password(), request.passwordConfirm());
        existEmail(request.email());

        User user = createUser(request);
        userRepositoryPort.save(user);
        emailVerifyRepositoryPort.deleteByEmail(request.email());
    }

    @Override
    @Transactional
    public TokenResponse signIn(SignInRequest request) {
        User user = userRepositoryPort.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));

        validatePassword(request.password(), user.getLocalAccount().getPassword());

        TokenResponse tokenResponse = tokenService.generate(user.getId(),
            user.getRole());
        tokenService.saveRefreshToken(user.getId(), tokenResponse.refreshToken());

        return tokenResponse;
    }

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

    private void isEmailVerified(String email) {
        EmailVerify emailVerify = emailVerifyRepositoryPort.findByEmail(email)
            .orElseThrow(() -> new EmailVerifyNotFoundException("이메일 인증을 받지 않았습니다."));
        if (!emailVerify.isVerified()) {
            throw new InvalidCodeException("이메일 인증을 받지 않았습니다.");
        }
    }

    private void passwordCheck(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new PasswordMismatchException("패스워드와 패스워드확인이 일치하지 않습니다.");
        }
    }

    private void existEmail(String email) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
        }
    }

    private User createUser(SignUpRequest request) {
        LocalDateTime now = dateTimeUtil.now();

        LocalAccount localAccount = LocalAccount.builder()
            .email(request.email())
            .password(passwordEncode(request.password()))
            .createdAt(now)
            .updatedAt(now)
            .build();

        UserInfo userInfo = UserInfo.builder()
            .name(request.nickname())
            .phone("")
            .createdAt(now)
            .updatedAt(now)
            .build();

        return User.builder()
            .nickname(request.nickname())
            .role(UserRole.valueOf(request.userRole()))
            .localAccount(localAccount)
            .userInfo(userInfo)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private String generateAuthCode(String email) {
        return randomUtil.generateRandomString();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!authenticationService.matches(rawPassword, encodedPassword)) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다");
        }
    }

    private String passwordEncode(String password) {
        return authenticationService.encode(password);
    }
} 
package com.pts.api.user.service;

import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.UserRole;
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
import com.pts.api.user.exception.PasswordMismatchException;
import com.pts.api.user.exception.PasswordNotMatchedException;
import com.pts.api.user.exception.UserNotFoundException;
import com.pts.api.user.model.EmailVerify;
import com.pts.api.user.model.LocalAccount;
import com.pts.api.user.model.User;
import com.pts.api.user.model.UserInfo;
import com.pts.api.user.repository.EmailVerifyRepository;
import com.pts.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerifyRepository emailVerifyRepository;
    private final IDateTimeUtil dateTimeUtil;
    private final TokenService tokenService;
    private final IRandomUtil randomUtil;
    private final AuthenticationService authenticationService;
    private final OutboxPublisher outboxPublisher;

    @Transactional
    public void signUp(SignUpRequestDto request) {
        isEmailVerified(request.email());
        passwordCheck(request.password(), request.passwordConfirm());
        existEmail(request.email());

        userRepository.save(createUser(request));
        emailVerifyRepository.deleteById(request.email());
    }

    @Transactional
    public TokenResponseDto signIn(SignInRequestDto request) {
        User user = userRepository.findOneByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));

        validatePassword(request.password(), user.getLocalAccount().getPassword());

        TokenResponseDto tokenResponseDto = tokenService.generate(user.getId(),
            user.getRole());
        tokenService.saveRefreshToken(user.getId(), tokenResponseDto.refreshToken());

        return tokenResponseDto;
    }

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

    @Transactional
    public void confirm(AuthCodeConfirmRequestDto request) {
        acquireLockOrThrow(request.email());
        try {
            EmailVerify emailVerify = emailVerifyRepository.findById(request.email())
                .orElseThrow(() -> new EmailVerifyNotFoundException("이메일 인증 정보를 찾을 수 없습니다."));

            if (emailVerify.isOverTryCount()) {
                emailVerifyRepository.deleteById(request.email());
                throw new InvalidCodeException("인증 시도 횟수를 초과했습니다.");
            }

            if (!emailVerify.getAuthCode().equals(request.authCode())) {
                emailVerify.incrementTryCount();
                emailVerifyRepository.save(emailVerify);
                throw new InvalidCodeException("인증 코드가 일치하지 않습니다.");
            }

            emailVerify.setVerified(true);
            emailVerifyRepository.save(emailVerify);
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
            .authCode(authCode)
            .tryCount(INIT_TRY_COUNT)
            .verified(false)
            .build();

        emailVerifyRepository.save(emailVerify);
    }

    private void acquireLockOrThrow(String email) {
        if (!emailVerifyRepository.getLock(email)) {
            throw new EmailVerifyLockedException("이메일 인증을 처리중입니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private void releaseLock(String email) {
        emailVerifyRepository.releaseLock(email);
    }


    private void isEmailVerified(String email) {
        EmailVerify emailVerify = emailVerifyRepository.findById(email)
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
        if (userRepository.findOneByEmail(email).isPresent()) {
            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
        }
    }

    private User createUser(SignUpRequestDto request) {
        LocalDateTime now = dateTimeUtil.now();

        return User.builder()
            .nickname(request.nickname())
            .role(UserRole.valueOf(request.userRole()))
            .localAccount(
                LocalAccount.builder()
                    .email(request.email())
                    .password(passwordEncode(request.password()))
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
            )
            .userInfo(
                UserInfo.builder()
                    .fullName(request.nickname())
                    .phone("")
                    .address("")
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
            )
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
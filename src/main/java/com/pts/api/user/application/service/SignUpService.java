package com.pts.api.user.application.service;

import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.user.application.dto.request.SignUpRequestDto;
import com.pts.api.user.application.port.in.SignUpUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.PasswordEncoderPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.PasswordMismatchException;
import com.pts.api.user.domain.model.EmailVerify;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final IDateTimeUtil dateTimeUtil;

    @Override
    @Transactional
    public void execute(SignUpRequestDto signUpRequestDto) {
        isEmailVerified(signUpRequestDto.email());
        passwordCheck(signUpRequestDto.password(), signUpRequestDto.passwordConfirm());
        existEmail(signUpRequestDto.email());

        userRepositoryPort.save(createUser(signUpRequestDto));
        emailVerifyRepositoryPort.deleteById(signUpRequestDto.email());
    }

    private void isEmailVerified(String email) {
        EmailVerify emailVerify = emailVerifyRepositoryPort.findById(email).
            orElseThrow(() -> new EmailVerifyNotFoundException("이메일 인증을 받지 않았습니다."));
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
        if (userRepositoryPort.findOneByEmail(email).isPresent()) {
            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoderPort.encode(password);
    }

    private User createUser(SignUpRequestDto signUpRequestDto) {
        LocalDateTime now = dateTimeUtil.now();

        return User.builder()
            .nickname(signUpRequestDto.nickname())
            .role(UserRole.valueOf(signUpRequestDto.userRole()))
            .localAccount(
                LocalAccount.builder()
                    .email(signUpRequestDto.email())
                    .password(passwordEncoderPort.encode(signUpRequestDto.password()))
                    .createdAt(now)
                    .updatedAt(now)
                    .build()
            )
            .build();
    }
}

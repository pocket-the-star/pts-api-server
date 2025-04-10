package com.pts.api.user.application.service;

import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.lib.internal.shared.util.date.IDateTimeUtil;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.port.in.SignUpUseCase;
import com.pts.api.user.application.port.out.EmailVerifyRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.AlreadyExistsException;
import com.pts.api.user.common.exception.EmailVerifyNotFoundException;
import com.pts.api.user.common.exception.InvalidCodeException;
import com.pts.api.user.common.exception.PasswordMismatchException;
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
public class SignUpApplicationService implements SignUpUseCase {

    private final EmailVerifyRepositoryPort emailVerifyRepositoryPort;
    private final IDateTimeUtil dateTimeUtil;
    private final AuthenticationService authenticationService;
    private final UserRepositoryPort userRepositoryPort;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청
     */
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

    /**
     * 이메일 인증 확인
     *
     * @param email 이메일
     */
    private void isEmailVerified(String email) {
        EmailVerify emailVerify = emailVerifyRepositoryPort.findByEmail(email)
            .orElseThrow(() -> new EmailVerifyNotFoundException("이메일 인증을 받지 않았습니다."));
        if (!emailVerify.isVerified()) {
            throw new InvalidCodeException("이메일 인증을 받지 않았습니다.");
        }
    }

    /**
     * 비밀번호 확인
     *
     * @param password        비밀번호
     * @param passwordConfirm 비밀번호 확인
     */
    private void passwordCheck(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new PasswordMismatchException("패스워드와 패스워드확인이 일치하지 않습니다.");
        }
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 이메일
     */
    private void existEmail(String email) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
        }
    }

    /**
     * 회원가입 사용자 생성
     *
     * @param request 회원가입 요청
     * @return 사용자
     */
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

    /**
     * 비밀번호 암호화
     *
     * @param password 비밀번호
     * @return 암호화된 비밀번호
     */
    private String passwordEncode(String password) {
        return authenticationService.encode(password);
    }
} 
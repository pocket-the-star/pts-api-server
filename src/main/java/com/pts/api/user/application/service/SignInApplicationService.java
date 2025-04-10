package com.pts.api.user.application.service;

import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.in.SignInUseCase;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.common.exception.PasswordNotMatchedException;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplicationService implements SignInUseCase {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;
    private final UserRepositoryPort userRepositoryPort;

    /**
     * 로그인
     *
     * @param request 로그인 요청
     * @return 토큰 응답
     */
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

    /**
     * 비밀번호 확인
     *
     * @param rawPassword     입력 비밀번호
     * @param encodedPassword 저장된 비밀번호
     */
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!authenticationService.matches(rawPassword, encodedPassword)) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다");
        }
    }
}
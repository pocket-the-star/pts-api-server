package com.pts.api.user.application.service;

import com.pts.api.user.application.dto.request.SignInRequestDto;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.in.SignInUseCase;
import com.pts.api.user.application.port.out.PasswordEncoderPort;
import com.pts.api.user.application.port.out.RefreshTokenRepositoryPort;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.application.service.internal.GenerateTokenService;
import com.pts.api.user.common.exception.PasswordNotMatchedException;
import com.pts.api.user.common.exception.UserNotFoundException;
import com.pts.api.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignInService implements SignInUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final GenerateTokenService generateTokenService;
    private final RefreshTokenRepositoryPort refreshTokenPort;

    @Override
    @Transactional
    public TokenResponseDto execute(SignInRequestDto request) {
        User user = userRepositoryPort.findOneByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다"));

        validatePassword(request.password(), user.getLocalAccount().getPassword());

        TokenResponseDto tokenResponseDto = generateTokenService.generate(user.getId(),
            user.getRole());
        refreshTokenPort.save(user.getId(), tokenResponseDto.refreshToken());

        return tokenResponseDto;
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoderPort.matches(rawPassword, encodedPassword)) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다");
        }
    }
}

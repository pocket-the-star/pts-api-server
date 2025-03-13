package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.SignInRequestDto;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.in.SignInUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "로그인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class SignInController {

    private final SignInUseCase signInUseCase;

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<TokenResponseDto>> signIn(
        @Valid @RequestBody SignInRequestDto request
    ) {
        TokenResponseDto tokenResponseDto = signInUseCase.execute(request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, tokenResponseDto,
            tokenResponseDto.refreshToken());
    }
}

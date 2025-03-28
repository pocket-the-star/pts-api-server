package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;
import com.pts.api.user.application.dto.request.EmailVerifyRequest;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.in.ConfirmEmailUseCase;
import com.pts.api.user.application.port.in.SignInUseCase;
import com.pts.api.user.application.port.in.SignUpUseCase;
import com.pts.api.user.application.port.in.VerifyEmailUseCase;
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

@Tag(name = "회원", description = "회원 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    private final ConfirmEmailUseCase confirmEmailUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<Void>> signUp(
        @Valid @RequestBody SignUpRequest request) {
        signUpUseCase.signUp(request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<TokenResponse>> signIn(
        @Valid @RequestBody SignInRequest request) {
        TokenResponse response = signInUseCase.signIn(request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, response,
            response.refreshToken());
    }

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 코드를 발송합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(
        @Valid @RequestBody EmailVerifyRequest request) {
        verifyEmailUseCase.verifyEmail(request.email());
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "인증 코드 확인", description = "이메일 인증 코드를 확인합니다.")
    @PostMapping("/auth-code/confirm")
    public ResponseEntity<BaseResponse<Void>> confirmAuthCode(
        @Valid @RequestBody AuthCodeConfirmRequest request) {
        confirmEmailUseCase.confirm(request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
} 
package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
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

@Tag(name = "회원", description = "회원 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SignInController {

    private final SignInUseCase signInUseCase;

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<TokenResponse>> signIn(
        @Valid @RequestBody SignInRequest request) {
        TokenResponse response = signInUseCase.signIn(request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, response,
            response.refreshToken());
    }
}
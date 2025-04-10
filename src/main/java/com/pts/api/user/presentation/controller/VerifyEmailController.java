package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.EmailVerifyRequest;
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
public class VerifyEmailController {

    private final VerifyEmailUseCase verifyEmailUseCase;

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 코드를 발송합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(
        @Valid @RequestBody EmailVerifyRequest request) {
        verifyEmailUseCase.verifyEmail(request.email());
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}
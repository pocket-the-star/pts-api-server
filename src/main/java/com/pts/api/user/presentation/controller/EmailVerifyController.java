package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.EmailVerifyRequestDto;
import com.pts.api.user.application.port.in.EmailVerifyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "이메일 인증 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class EmailVerifyController {

    private final EmailVerifyUseCase emailVerifyUseCase;

    @Operation(summary = "이메일 인증", description = "이메일 인증을 진행합니다.")
    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(
        @Valid @RequestBody EmailVerifyRequestDto request) {
        log.info("verifyEmail request: {}", request);
        emailVerifyUseCase.verifyEmail(request.email());
        log.info("verifyEmail request: {}", request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

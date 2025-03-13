package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.application.port.in.AuthCodeConfirmUseCase;
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

@Tag(name = "회원", description = "인증번호 확인 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthCodeConfirmController {

    private final AuthCodeConfirmUseCase authCodeConfirmUseCase;

    @Operation(summary = "인증번호 확인", description = "발급받은 인증번호 확인")
    @PostMapping("/auth-code/confirm")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(
        @Valid @RequestBody AuthCodeConfirmRequestDto request) {
        authCodeConfirmUseCase.confirm(request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

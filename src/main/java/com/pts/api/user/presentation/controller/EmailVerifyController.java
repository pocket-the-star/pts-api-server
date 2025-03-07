package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.EmailVerifyRequestDto;
import com.pts.api.user.application.port.in.EmailVerifyUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class EmailVerifyController {

    private final EmailVerifyUseCase emailVerifyUseCase;

    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<Void>> verifyEmail(
        @Valid @RequestBody EmailVerifyRequestDto request) {
        emailVerifyUseCase.verifyEmail(request.email());

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

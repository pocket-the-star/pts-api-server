package com.pts.api.user.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.SignUpRequestDto;
import com.pts.api.user.application.port.in.SignUpUseCase;
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

@Tag(name = "회원", description = "회원가입 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpUseCase signUpUseCase;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<Void>> signUp(
        @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        signUpUseCase.execute(signUpRequestDto);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

}

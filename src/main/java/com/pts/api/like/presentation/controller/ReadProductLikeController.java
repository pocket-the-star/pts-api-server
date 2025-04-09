package com.pts.api.like.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.like.application.port.in.ReadProductLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요", description = "상품 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReadProductLikeController {

    private final ReadProductLikeUseCase readProductLikeUseCase;

    @Operation(summary = "좋아요 상태 조회", description = "상품의 좋아요 상태를 조회합니다.")
    @GetMapping("/private/products/{productId}/like")
    public ResponseEntity<BaseResponse<Boolean>> getLikeStatus(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readProductLikeUseCase.isLiked(productId, userId));
    }
}

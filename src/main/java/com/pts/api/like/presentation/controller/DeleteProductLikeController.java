package com.pts.api.like.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.like.application.port.in.DeleteProductLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요", description = "상품 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/products/{productId}/likes")
public class DeleteProductLikeController {

    private final DeleteProductLikeUseCase deleteProductLikeUseCase;

    @Operation(summary = "좋아요 취소", description = "상품 좋아요를 취소합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId) {

        deleteProductLikeUseCase.unlike(productId, userId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

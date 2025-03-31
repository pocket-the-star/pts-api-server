package com.pts.api.like.presentation.controller;


import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.like.application.port.in.ProductLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요", description = "상품 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/{productId}/likes")
public class ProductLikeController {

    private final ProductLikeUseCase productLikeUseCase;

    @Operation(summary = "좋아요 상태 조회", description = "상품의 좋아요 상태를 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<Boolean>> getLikeStatus(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId) {

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            productLikeUseCase.isLiked(productId, userId));
    }

    @Operation(summary = "좋아요", description = "상품을 좋아요합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> post(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId) {

        productLikeUseCase.like(productId, userId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "좋아요 취소", description = "상품 좋아요를 취소합니다.")
    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long productId) {

        productLikeUseCase.unlike(productId, userId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

}

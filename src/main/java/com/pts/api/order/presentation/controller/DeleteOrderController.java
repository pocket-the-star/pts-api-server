package com.pts.api.order.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.order.application.port.in.DeleteOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/v1/private/orders")
@RequiredArgsConstructor
public class DeleteOrderController {

    private final DeleteOrderUseCase deleteOrderUseCase;

    @Operation(summary = "주문 삭제")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @Parameter(description = "주문 ID") @PathVariable Long orderId) {
        deleteOrderUseCase.delete(userId, orderId);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

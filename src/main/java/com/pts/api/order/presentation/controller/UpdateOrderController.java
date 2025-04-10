package com.pts.api.order.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.order.application.dto.request.UpdateOrderRequest;
import com.pts.api.order.application.port.in.UpdateOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/v1/private/orders")
@RequiredArgsConstructor
public class UpdateOrderController {

    private final UpdateOrderUseCase updateOrderUseCase;

    @Operation(summary = "주문 상태 수정")
    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> updateStatus(
        @AuthenticationPrincipal Long userId,
        @Parameter(description = "주문 ID") @PathVariable Long orderId,
        @Valid @RequestBody UpdateOrderRequest request) {
        updateOrderUseCase.updateStatus(userId, orderId, request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

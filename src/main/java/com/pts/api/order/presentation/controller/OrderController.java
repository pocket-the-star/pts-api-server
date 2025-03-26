package com.pts.api.order.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.UpdateOrderRequest;
import com.pts.api.order.application.port.in.CreateOrderUseCase;
import com.pts.api.order.application.port.in.UpdateOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;

    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateOrderRequest request) {
        createOrderUseCase.create(userId, request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 상태 수정")
    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> updateStatus(
        @AuthenticationPrincipal Long userId,
        @Parameter(description = "주문 ID") @PathVariable Long orderId,
        @Valid @RequestBody UpdateOrderRequest request) {
        updateOrderUseCase.updateStatus(userId, orderId, request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 취소")
    @PatchMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> cancel(
        @AuthenticationPrincipal Long userId,
        @Parameter(description = "주문 ID") @PathVariable Long orderId) {
        updateOrderUseCase.cancel(userId, orderId);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 삭제")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @Parameter(description = "주문 ID") @PathVariable Long orderId) {
        updateOrderUseCase.delete(userId, orderId);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
} 
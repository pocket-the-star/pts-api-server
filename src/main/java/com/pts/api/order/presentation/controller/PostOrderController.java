package com.pts.api.order.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.port.in.PostOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/api/v1/private/orders")
@RequiredArgsConstructor
public class PostOrderController {

    private final PostOrderUseCase postOrderUseCase;

    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateOrderRequest request) {
        postOrderUseCase.create(userId, request);
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
} 
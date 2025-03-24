package com.pts.api.order.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.order.dto.request.CreateOrderRequestDto;
import com.pts.api.order.dto.request.UpdateOrderRequestDto;
import com.pts.api.order.service.OrderFacade;
import com.pts.api.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "주문", description = "주문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> post(
        @AuthenticationPrincipal Long userId,
        @Valid @RequestBody CreateOrderRequestDto request) {

        orderFacade.create(userId, request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 수정", description = "주문을 수정합니다.")
    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> put(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long orderId,
        @Valid @RequestBody UpdateOrderRequestDto request) {

        orderService.updateStatus(userId, orderId, request.orderStatus());

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    @PatchMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> put(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long orderId
    ) {

        orderService.cancel(userId, orderId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다.")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse<Void>> delete(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long orderId) {

        orderService.delete(userId, orderId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
}

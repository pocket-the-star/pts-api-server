package com.pts.api.product.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.port.in.PostProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequestMapping("/api/v1/private/products")
@RequiredArgsConstructor
public class PostProductController {

    private final PostProductUseCase postProductUseCase;

    @Operation(summary = "상품 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(@RequestBody CreateProductRequest request) {
        postProductUseCase.create(request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }
} 
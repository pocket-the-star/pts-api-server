package com.pts.api.product.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.dto.response.ProductResponse;
import com.pts.api.product.application.port.in.CreateProductUseCase;
import com.pts.api.product.application.port.in.ReadProductListUseCase;
import com.pts.api.product.application.port.in.ReadProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ReadProductUseCase readProductUseCase;
    private final ReadProductListUseCase readProductListUseCase;

    @Operation(summary = "상품 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(@RequestBody CreateProductRequest request) {
        createProductUseCase.create(request);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK);
    }

    @Operation(summary = "상품 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> findById(@PathVariable Long id) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, readProductUseCase.findById(id));
    }

    @Operation(summary = "상품 목록 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductResponse>>> findAll(
        @Parameter(description = "idol ID") @RequestParam(required = false) Long idolId,
        @Parameter(description = "카테고리 ID") @RequestParam(required = false) Long categoryId,
        @Parameter(description = "서브카테고리 ID") @RequestParam(required = false) Long subCategoryId,
        @Parameter(description = "페이지 오프셋") @RequestParam(defaultValue = "0") Long offset,
        @Parameter(description = "페이지 사이즈") @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readProductListUseCase.findAll(idolId, categoryId, subCategoryId,
                offset, limit));
    }
} 
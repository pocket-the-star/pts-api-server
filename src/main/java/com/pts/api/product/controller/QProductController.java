package com.pts.api.product.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.product.dto.response.GetProductResponseDto;
import com.pts.api.product.service.QProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품", description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class QProductController {

    private final QProductService QProductService;

    @Operation(summary = "상품 조회", description = "상품 목록을 조회합니다.")
    @GetMapping("/products")
    public ResponseEntity<BaseResponse<List<GetProductResponseDto>>> getProducts(
        @RequestParam(value = "artistId", required = false) Long artistId,
        @RequestParam(value = "categoryId", required = false) Long categoryId,
        @RequestParam(value = "subCategoryId", required = false) Long subCategoryId,
        @RequestParam(value = "offset", defaultValue = "0") int offset
    ) {

        return ResponseGenerator.ok(ResponseMsg.OK,
            HttpStatus.OK,
            QProductService.getProducts(artistId, categoryId, subCategoryId, offset));
    }
}

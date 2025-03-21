package com.pts.api.category.controller;

import com.pts.api.category.dto.response.GetCategoryResponseDto;
import com.pts.api.category.service.CategoryService;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리", description = "카테고리 API")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 조회", description = "카테고리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetCategoryResponseDto>>> getCategories() {
        List<GetCategoryResponseDto> categories = categoryService.getCategories();

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, categories);
    }

    @Operation(summary = "카테고리 상세 조회", description = "카테고리 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetCategoryResponseDto>> getCategory(@PathVariable Long id) {
        GetCategoryResponseDto category = categoryService.getCategory(id);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, category);
    }
}

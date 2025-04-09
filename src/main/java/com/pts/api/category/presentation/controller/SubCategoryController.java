package com.pts.api.category.presentation.controller;


import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import com.pts.api.category.application.service.SubCategoryApplicationService;
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

@Tag(name = "카테고리", description = "서브 카테고리 API")
@RestController
@RequestMapping("/api/v1/categories/{categoryId}")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryApplicationService subCategoryApplicationService;

    @Operation(summary = "서브 카테고리 조회", description = "서브 카테고리 목록을 조회합니다.")
    @GetMapping("/sub-categories")
    public ResponseEntity<BaseResponse<List<ReadSubCategoryResponse>>> getSubCategories(
        @PathVariable Long categoryId) {
        List<ReadSubCategoryResponse> subCategories = subCategoryApplicationService.getSubCategories(
            categoryId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, subCategories);
    }

    @Operation(summary = "서브 카테고리 상세 조회", description = "서브 카테고리 상세 정보를 조회합니다.")
    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<BaseResponse<ReadSubCategoryResponse>> getSubCategory(
        @PathVariable Long categoryId,
        @PathVariable Long id) {
        ReadSubCategoryResponse subCategory = subCategoryApplicationService.getSubCategory(
            categoryId,
            id);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, subCategory);
    }
}

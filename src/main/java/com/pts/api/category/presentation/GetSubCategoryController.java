package com.pts.api.category.presentation;


import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.application.port.in.GetSubCategoryUseCase;
import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories/{categoryId}")
@RequiredArgsConstructor
public class GetSubCategoryController {

    private final GetSubCategoryUseCase getSubCategoryUseCase;

    @GetMapping("/sub-categories")
    public ResponseEntity<BaseResponse<List<GetSubCategoryResponseDto>>> getSubCategories(
        @PathVariable Long categoryId) {
        List<GetSubCategoryResponseDto> subCategories = getSubCategoryUseCase.getSubCategories(
            categoryId);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, subCategories);
    }

    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<BaseResponse<GetSubCategoryResponseDto>> getSubCategory(
        @PathVariable Long categoryId,
        @PathVariable Long id) {
        GetSubCategoryResponseDto subCategory = getSubCategoryUseCase.getSubCategory(categoryId,
            id);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, subCategory);
    }
}

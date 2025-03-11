package com.pts.api.category.presentation;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.category.application.port.in.GetCategoryUseCase;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class GetCategoryController {

    private final GetCategoryUseCase getCategoryUseCase;

    @GetMapping
    public ResponseEntity<BaseResponse<List<GetCategoryResponseDto>>> getCategories() {
        List<GetCategoryResponseDto> categories = getCategoryUseCase.getCategories();

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<GetCategoryResponseDto>> getCategory(@PathVariable Long id) {
        GetCategoryResponseDto category = getCategoryUseCase.getCategory(id);

        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, category);
    }
}

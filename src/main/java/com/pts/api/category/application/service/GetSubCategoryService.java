package com.pts.api.category.application.service;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.application.port.in.GetSubCategoryUseCase;
import com.pts.api.category.application.port.out.ReadSubCategoryRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetSubCategoryService implements GetSubCategoryUseCase {

    private final ReadSubCategoryRepositoryPort readSubCategoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetSubCategoryResponseDto getSubCategory(Long categoryId, Long id) {
        return readSubCategoryRepositoryPort.findOneById(categoryId, id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetSubCategoryResponseDto> getSubCategories(Long categoryId) {
        return readSubCategoryRepositoryPort.findAllByCategoryId(categoryId);
    }

}

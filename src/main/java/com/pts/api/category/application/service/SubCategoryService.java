package com.pts.api.category.application.service;

import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import com.pts.api.category.application.port.in.ReadSubCategoryUseCase;
import com.pts.api.category.application.port.out.SubCategoryRepositoryPort;
import com.pts.api.category.domain.model.SubCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubCategoryService implements ReadSubCategoryUseCase {

    private final SubCategoryRepositoryPort subCategoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ReadSubCategoryResponse getSubCategory(Long categoryId, Long id) {
        return mapToDto(subCategoryRepositoryPort.findOneById(categoryId, id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadSubCategoryResponse> getSubCategories(Long categoryId) {
        return subCategoryRepositoryPort.findAllByCategoryId(categoryId).stream()
            .map(this::mapToDto)
            .toList();
    }

    private ReadSubCategoryResponse mapToDto(SubCategory subCategory) {
        return new ReadSubCategoryResponse(
            subCategory.getId(),
            subCategory.getName(),
            subCategory.getCategoryId(),
            subCategory.getCreatedAt(),
            subCategory.getUpdatedAt()
        );
    }

}

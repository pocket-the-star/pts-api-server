package com.pts.api.category.service;

import com.pts.api.category.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.model.SubCategory;
import com.pts.api.category.repository.SubCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepositoryPort;

    @Transactional(readOnly = true)
    public GetSubCategoryResponseDto getSubCategory(Long categoryId, Long id) {
        return mapToDto(subCategoryRepositoryPort.findOneById(categoryId, id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public List<GetSubCategoryResponseDto> getSubCategories(Long categoryId) {
        return subCategoryRepositoryPort.findAllByCategoryId(categoryId).stream()
            .map(this::mapToDto)
            .toList();
    }

    private GetSubCategoryResponseDto mapToDto(SubCategory subCategory) {
        return new GetSubCategoryResponseDto(
            subCategory.getId(),
            subCategory.getName(),
            subCategory.getCategoryId(),
            subCategory.getCreatedAt(),
            subCategory.getUpdatedAt()
        );
    }

}

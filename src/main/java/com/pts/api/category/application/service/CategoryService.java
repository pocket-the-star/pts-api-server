package com.pts.api.category.application.service;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import com.pts.api.category.application.port.in.ReadCategoryUseCase;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import com.pts.api.category.infrastructure.persistence.entity.CategoryEntity;
import com.pts.api.category.infrastructure.persistence.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ReadCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public ReadCategoryResponse getCategory(Long id) {
        return mapToDto(categoryRepository.findOneById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadCategoryResponse> getCategories() {
        return categoryRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private ReadCategoryResponse mapToDto(CategoryEntity categoryEntity) {
        return new ReadCategoryResponse(
            categoryEntity.getId(),
            categoryEntity.getName(),
            categoryEntity.getCreatedAt(),
            categoryEntity.getUpdatedAt()
        );
    }

}

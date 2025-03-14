package com.pts.api.category.service;

import com.pts.api.category.common.exception.CategoryNotFoundException;
import com.pts.api.category.dto.response.GetCategoryResponseDto;
import com.pts.api.category.model.Category;
import com.pts.api.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public GetCategoryResponseDto getCategory(Long id) {
        return mapToDto(categoryRepository.findOneById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> getCategories() {
        return categoryRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private GetCategoryResponseDto mapToDto(Category category) {
        return new GetCategoryResponseDto(
            category.getId(),
            category.getName(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }

}

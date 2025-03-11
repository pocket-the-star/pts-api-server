package com.pts.api.category.infra.mapper;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.lib.external.jpa.category.model.CategoryEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadCategoryMapper implements IReadCategoryMapper {

    public GetCategoryResponseDto mapToCategory(CategoryEntity categoryEntity) {
        return new GetCategoryResponseDto(
            categoryEntity.getId(),
            categoryEntity.getName(),
            categoryEntity.getCreatedAt(),
            categoryEntity.getUpdatedAt()
        );
    }

    public List<GetCategoryResponseDto> mapToCategories(List<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
            .map(this::mapToCategory)
            .collect(Collectors.toList());
    }
}

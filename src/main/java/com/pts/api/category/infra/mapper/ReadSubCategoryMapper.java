package com.pts.api.category.infra.mapper;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.lib.external.jpa.category.model.SubCategoryEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReadSubCategoryMapper implements IReadSubCategoryMapper {

    public GetSubCategoryResponseDto mapToSubCategory(SubCategoryEntity subCategoryEntity) {

        return new GetSubCategoryResponseDto(subCategoryEntity.getId(), subCategoryEntity.getName(),
            subCategoryEntity.getCategoryId(), subCategoryEntity.getCreatedAt(),
            subCategoryEntity.getUpdatedAt());
    }

    public List<GetSubCategoryResponseDto> mapToSubCategories(
        List<SubCategoryEntity> subCategoryEntities) {

        return subCategoryEntities.stream().map(this::mapToSubCategory)
            .collect(Collectors.toList());
    }

}

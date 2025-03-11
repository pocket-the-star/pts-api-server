package com.pts.api.category.infra.mapper;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.lib.external.jpa.category.model.CategoryEntity;
import java.util.List;

public interface IReadCategoryMapper {

    GetCategoryResponseDto mapToCategory(CategoryEntity categoryEntity);

    List<GetCategoryResponseDto> mapToCategories(List<CategoryEntity> categoryEntities);
}

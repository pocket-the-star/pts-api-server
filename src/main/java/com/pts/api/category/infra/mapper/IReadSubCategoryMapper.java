package com.pts.api.category.infra.mapper;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.lib.external.jpa.category.model.SubCategoryEntity;
import java.util.List;

public interface IReadSubCategoryMapper {

    GetSubCategoryResponseDto mapToSubCategory(SubCategoryEntity subCategoryEntity);

    List<GetSubCategoryResponseDto> mapToSubCategories(List<SubCategoryEntity> subCategoryEntities);
}

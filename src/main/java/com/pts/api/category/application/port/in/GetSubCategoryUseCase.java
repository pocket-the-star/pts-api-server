package com.pts.api.category.application.port.in;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import java.util.List;

public interface GetSubCategoryUseCase {

    GetSubCategoryResponseDto getSubCategory(Long categoryId, Long id);

    List<GetSubCategoryResponseDto> getSubCategories(Long categoryId);

}

package com.pts.api.category.application.port.in;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import java.util.List;

public interface GetCategoryUseCase {

    GetCategoryResponseDto getCategory(Long id);

    List<GetCategoryResponseDto> getCategories();
}

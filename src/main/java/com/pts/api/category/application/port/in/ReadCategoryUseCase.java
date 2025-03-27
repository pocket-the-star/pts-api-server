package com.pts.api.category.application.port.in;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import java.util.List;

public interface ReadCategoryUseCase {

    ReadCategoryResponse getCategory(Long id);

    List<ReadCategoryResponse> getCategories();
}
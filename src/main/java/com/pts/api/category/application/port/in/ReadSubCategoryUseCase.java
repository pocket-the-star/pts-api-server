package com.pts.api.category.application.port.in;

import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import java.util.List;

public interface ReadSubCategoryUseCase {

    ReadSubCategoryResponse getSubCategory(Long categoryId, Long id);


    List<ReadSubCategoryResponse> getSubCategories(Long categoryId);

}

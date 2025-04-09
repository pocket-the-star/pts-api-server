package com.pts.api.category.application.port.dto.response;

import com.pts.api.category.domain.model.SubCategory;
import java.time.LocalDateTime;

public record ReadSubCategoryResponse(Long id, String name, Long categoryId,
                                      LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {

    public static ReadSubCategoryResponse fromModel(SubCategory subCategory) {
        return new ReadSubCategoryResponse(
            subCategory.getId(),
            subCategory.getName(),
            subCategory.getCategoryId(),
            subCategory.getCreatedAt(),
            subCategory.getUpdatedAt()
        );
    }
}

package com.pts.api.category.application.port.dto.response;

import com.pts.api.category.domain.model.Category;
import java.time.LocalDateTime;

public record ReadCategoryResponse(Long id, String name, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {

    public static ReadCategoryResponse fromModel(Category category) {
        return new ReadCategoryResponse(
            category.getId(),
            category.getName(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }
}

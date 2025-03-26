package com.pts.api.product.application.dto.response;

import com.pts.api.product.domain.model.ProductImage;
import java.time.LocalDateTime;

public record ProductImageResponse(
    Long id,
    String imageUrl,
    Integer sortOrder,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ProductImageResponse from(ProductImage productImage) {
        return new ProductImageResponse(
            productImage.getId(),
            productImage.getImageUrl(),
            productImage.getSortOrder(),
            productImage.getCreatedAt(),
            productImage.getUpdatedAt()
        );
    }
} 
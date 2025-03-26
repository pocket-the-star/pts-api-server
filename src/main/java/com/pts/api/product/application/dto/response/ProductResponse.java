package com.pts.api.product.application.dto.response;

import com.pts.api.product.domain.model.Product;
import java.time.LocalDateTime;
import java.util.List;

public record ProductResponse(
    Long id,
    String title,
    Long subCategoryId,
    Long artistId,
    Integer minBuyPrice,
    Integer maxSellPrice,
    List<ProductImageResponse> images,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getTitle(),
            product.getSubCategoryId(),
            product.getArtistId(),
            product.getMinBuyPrice(),
            product.getMaxSellPrice(),
            product.getImages().stream()
                .map(ProductImageResponse::from)
                .toList(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
} 
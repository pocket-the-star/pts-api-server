package com.pts.api.product.application.dto.response;

import java.time.LocalDateTime;

public record GetProductResponseDto(
    Long id,
    String title,
    String imageUrl,
    Long groupId,
    Long categoryId,
    Long subCategoryId,
    Long minBuyPrice,
    Long maxSellPrice,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}

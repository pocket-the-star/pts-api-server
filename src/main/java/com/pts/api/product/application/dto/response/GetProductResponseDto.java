package com.pts.api.product.application.dto.response;

public record GetProductResponseDto(
    Long id,
    String name,
    String imageUrl,
    Long groupId,
    Long categoryId,
    Long subCategoryId,
    int minBuyPrice,
    int maxSellPrice,
    String createdAt,
    String updatedAt
) {
} 

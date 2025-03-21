package com.pts.api.product.dto.response;

import java.time.LocalDateTime;

public record GetProductResponseDto(
    Long id,
    String title,
    String imageUrl,
    Integer minBuyPrice,
    Integer maxSellPrice,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}

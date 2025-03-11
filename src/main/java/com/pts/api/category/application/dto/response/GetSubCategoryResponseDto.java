package com.pts.api.category.application.dto.response;

import java.time.LocalDateTime;

public record GetSubCategoryResponseDto(Long id, String name, Long categoryId,
                                        LocalDateTime createdAt,
                                        LocalDateTime updatedAt) {

}

package com.pts.api.category.application.dto.response;

import java.time.LocalDateTime;

public record GetCategoryResponseDto(Long id, String name, LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {

}

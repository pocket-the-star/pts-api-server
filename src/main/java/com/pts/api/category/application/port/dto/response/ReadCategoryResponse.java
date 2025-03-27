package com.pts.api.category.application.port.dto.response;

import java.time.LocalDateTime;

public record ReadCategoryResponse(Long id, String name, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {

}

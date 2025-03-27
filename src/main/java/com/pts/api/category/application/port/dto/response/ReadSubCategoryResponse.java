package com.pts.api.category.application.port.dto.response;

import java.time.LocalDateTime;

public record ReadSubCategoryResponse(Long id, String name, Long categoryId,
                                      LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {

}

package com.pts.api.idol.application.dto.response;

import com.pts.api.idol.infrastructure.persistence.entity.IdolEntity;
import java.time.LocalDateTime;

public record ReadIdolResponse(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadIdolResponse of(IdolEntity idolEntity) {
        return new ReadIdolResponse(
            idolEntity.getId(),
            idolEntity.getName(),
            idolEntity.getCreatedAt(),
            idolEntity.getUpdatedAt()
        );
    }
}

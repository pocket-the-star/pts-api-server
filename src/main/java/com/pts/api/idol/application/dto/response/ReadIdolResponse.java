package com.pts.api.idol.application.dto.response;

import com.pts.api.idol.domain.model.Idol;
import java.time.LocalDateTime;

public record ReadIdolResponse(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadIdolResponse of(Idol idolEntity) {
        return new ReadIdolResponse(
            idolEntity.getId(),
            idolEntity.getName(),
            idolEntity.getCreatedAt(),
            idolEntity.getUpdatedAt()
        );
    }
}

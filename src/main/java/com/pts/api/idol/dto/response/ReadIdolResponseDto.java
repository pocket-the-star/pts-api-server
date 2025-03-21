package com.pts.api.idol.dto.response;

import com.pts.api.idol.model.Idol;
import java.time.LocalDateTime;

public record ReadIdolResponseDto(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadIdolResponseDto of(Idol idol) {
        return new ReadIdolResponseDto(
            idol.getId(),
            idol.getName(),
            idol.getCreatedAt(),
            idol.getUpdatedAt()
        );
    }
}

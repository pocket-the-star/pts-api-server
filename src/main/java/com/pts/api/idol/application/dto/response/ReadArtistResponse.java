package com.pts.api.idol.application.dto.response;

import com.pts.api.idol.domain.model.Artist;
import java.time.LocalDateTime;

public record ReadArtistResponse(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadArtistResponse of(Artist artistEntity) {
        return new ReadArtistResponse(
            artistEntity.getId(),
            artistEntity.getName(),
            artistEntity.getCreatedAt(),
            artistEntity.getUpdatedAt()
        );
    }
}

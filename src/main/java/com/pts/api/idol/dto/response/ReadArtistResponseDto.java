package com.pts.api.idol.dto.response;

import com.pts.api.idol.model.Artist;
import java.time.LocalDateTime;

public record ReadArtistResponseDto(
    Long id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadArtistResponseDto of(Artist artist) {
        return new ReadArtistResponseDto(
            artist.getId(),
            artist.getName(),
            artist.getCreatedAt(),
            artist.getUpdatedAt()
        );
    }
}

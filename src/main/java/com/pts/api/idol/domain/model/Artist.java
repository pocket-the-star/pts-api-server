package com.pts.api.idol.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Artist extends BaseModel {

    private Long id;

    private Idol idol;

    private String name;

    @Builder
    public Artist(Long id, Idol idol, String name,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.idol = idol;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}

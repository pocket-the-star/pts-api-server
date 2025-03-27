package com.pts.api.category.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Category extends BaseModel {

    private Long id;
    private String name;

    @Builder
    public Category(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
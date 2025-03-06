package com.pts.api.category.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;

public class Category extends BaseModel {

    private Long id;
    private String name;
    private Long parentId;
    private int depth;

    public Category(Long id, String name, Long parentId, int depth,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.depth = depth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
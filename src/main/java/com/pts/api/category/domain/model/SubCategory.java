package com.pts.api.category.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SubCategory extends BaseModel {

    private Long id;
    private String name;
    private Long categoryId;

    @Builder
    public SubCategory(Long id, String name, Long categoryId, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

}

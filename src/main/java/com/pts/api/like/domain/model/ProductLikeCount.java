package com.pts.api.like.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductLikeCount extends BaseModel {

    private Long productId;
    private Long count;

    @Builder
    public ProductLikeCount(Long productId, Long count,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.productId = productId;
        this.count = count;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public void increment() {
        this.count++;
    }

    public void decrement() {
        this.count--;
    }
} 
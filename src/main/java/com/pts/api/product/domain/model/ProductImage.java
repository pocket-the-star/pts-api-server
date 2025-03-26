package com.pts.api.product.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseModel {

    private Long id;
    private String imageUrl;
    private int sortOrder;
    private Product product;

    @Builder
    public ProductImage(Long id, String imageUrl, int sortOrder, Product product,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.product = product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public void delete(LocalDateTime deletedAt) {
        markDeleted(deletedAt);
    }

    public void update(String imageUrl, int sortOrder, LocalDateTime updatedAt) {
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        markUpdated(updatedAt);
    }
} 
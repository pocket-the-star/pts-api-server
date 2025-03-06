package com.pts.api.product.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;

public class ProductImage extends BaseModel {
    private Long id;
    private Long productId;
    private String imageUrl;
    private int sortOrder;

    public ProductImage(Long id, Long productId, String imageUrl, int sortOrder,
                        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
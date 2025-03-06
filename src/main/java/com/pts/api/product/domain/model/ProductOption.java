package com.pts.api.product.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;

public class ProductOption extends BaseModel {
    private Long id;
    private Long productId;
    private String name;
    private String value;

    public ProductOption(Long id, Long productId, String name, String value,
                         LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.value = value;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
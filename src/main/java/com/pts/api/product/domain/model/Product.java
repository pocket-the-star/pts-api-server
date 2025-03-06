package com.pts.api.product.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import java.util.List;

public class Product extends BaseModel {
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private Long groupId;
    private String extraInfo;
    private List<ProductOption> options;
    private List<ProductImage> images;

    public Product(Long id, String title, String description, Long categoryId, Long groupId, 
                   String extraInfo, List<ProductOption> options, List<ProductImage> images,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.groupId = groupId;
        this.extraInfo = extraInfo;
        this.options = options;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
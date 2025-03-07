package com.pts.api.lib.external.jpa.product.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", name = "description", nullable = false)
    private String description;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    @Column(name = "group_id", nullable = false)
    private Long groupId;
    
    @Column(columnDefinition = "json", name = "extra_info", nullable = false)
    private String extraInfo;
    
    @OneToMany(mappedBy = "product")
    private List<ProductOptionEntity> options;
    
    @OneToMany(mappedBy = "product")
    private List<ProductImageEntity> images;
    
    @Builder
    public ProductEntity(Long id, String title, String description, Long categoryId, Long groupId, 
                         String extraInfo, List<ProductOptionEntity> options, List<ProductImageEntity> images,
                         OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
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
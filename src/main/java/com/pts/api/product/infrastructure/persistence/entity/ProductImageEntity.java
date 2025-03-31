package com.pts.api.product.infrastructure.persistence.entity;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.product.domain.model.ProductImage;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImageEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private int sortOrder;

    @ManyToOne()
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductEntity product;

    @Builder
    public ProductImageEntity(Long id, String imageUrl, int sortOrder, ProductEntity product,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.product = product;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ProductImageEntity fromModel(ProductImage productImage) {
        return ProductImageEntity.builder()
            .id(productImage.getId())
            .imageUrl(productImage.getImageUrl())
            .sortOrder(productImage.getSortOrder())
            .createdAt(productImage.getCreatedAt())
            .updatedAt(productImage.getUpdatedAt())
            .deletedAt(productImage.getDeletedAt())
            .build();
    }

    public ProductImage toModel() {
        return ProductImage.builder()
            .id(id)
            .imageUrl(imageUrl)
            .sortOrder(sortOrder)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
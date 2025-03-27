package com.pts.api.like.infrastructure.persistence.entity;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.like.domain.model.ProductLike;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "product_likes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productId", "userId"})
    })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public ProductLikeEntity(Long productId, Long userId, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.productId = productId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ProductLikeEntity fromModel(ProductLike productLike) {
        return ProductLikeEntity.builder()
            .productId(productLike.getProductId())
            .userId(productLike.getUserId())
            .createdAt(productLike.getCreatedAt())
            .updatedAt(productLike.getUpdatedAt())
            .deletedAt(productLike.getDeletedAt())
            .build();
    }

    public ProductLike toModel() {
        return ProductLike.builder()
            .id(id)
            .productId(productId)
            .userId(userId)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
}

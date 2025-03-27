package com.pts.api.like.infrastructure.persistence.entity;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.like.domain.model.ProductLikeCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_like_counts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeCountEntity extends BaseEntity {

    @Id
    private Long productId;
    private Long count;

    @Builder
    public ProductLikeCountEntity(Long productId, Long count, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
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

    public ProductLikeCount toDomain() {
        return ProductLikeCount.builder()
            .productId(productId)
            .count(count)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static ProductLikeCountEntity fromDomain(ProductLikeCount productLikeCount) {
        return ProductLikeCountEntity.builder()
            .productId(productLikeCount.getProductId())
            .count(productLikeCount.getCount())
            .createdAt(productLikeCount.getCreatedAt())
            .updatedAt(productLikeCount.getUpdatedAt())
            .deletedAt(productLikeCount.getDeletedAt())
            .build();
    }
}

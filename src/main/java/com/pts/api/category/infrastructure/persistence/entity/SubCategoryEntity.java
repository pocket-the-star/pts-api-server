package com.pts.api.category.infrastructure.persistence.entity;

import com.pts.api.category.domain.model.SubCategory;
import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Builder
    public SubCategoryEntity(Long id, String name, Long categoryId, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public SubCategory toModel() {
        return SubCategory.builder()
            .id(this.id)
            .name(this.name)
            .categoryId(this.categoryId)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .deletedAt(this.deletedAt)
            .build();
    }

    public static SubCategoryEntity fromModel(SubCategory subCategory) {
        return SubCategoryEntity.builder()
            .id(subCategory.getId())
            .name(subCategory.getName())
            .categoryId(subCategory.getCategoryId())
            .createdAt(subCategory.getCreatedAt())
            .updatedAt(subCategory.getUpdatedAt())
            .deletedAt(subCategory.getDeletedAt())
            .build();
    }
}

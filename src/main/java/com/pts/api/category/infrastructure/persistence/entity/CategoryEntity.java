package com.pts.api.category.infrastructure.persistence.entity;

import com.pts.api.category.domain.model.Category;
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
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public CategoryEntity(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Category toModel() {
        return Category.builder()
            .id(this.id)
            .name(this.name)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .deletedAt(this.deletedAt)
            .build();
    }

    public static CategoryEntity fromModel(Category category) {
        return CategoryEntity.builder()
            .id(category.getId())
            .name(category.getName())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .deletedAt(category.getDeletedAt())
            .build();
    }
} 
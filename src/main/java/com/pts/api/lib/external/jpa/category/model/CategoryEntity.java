package com.pts.api.lib.external.jpa.category.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

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
    
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "depth")
    private Integer depth;
    
    @Builder
    public CategoryEntity(Long id, String name, Long parentId, Integer depth,
                          OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.depth = depth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
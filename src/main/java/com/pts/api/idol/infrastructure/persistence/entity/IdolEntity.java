package com.pts.api.idol.infrastructure.persistence.entity;

import com.pts.api.idol.domain.model.Idol;
import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "idols", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id", "name"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdolEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "idolEntity")
    private List<ArtistEntity> artistEntities;

    @Builder
    public IdolEntity(Long id, String name, List<ArtistEntity> artistEntities,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.artistEntities = artistEntities;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static IdolEntity fromModel(Idol idol) {
        return IdolEntity.builder()
            .id(idol.getId())
            .name(idol.getName())
            .artistEntities(idol.getArtists().stream().map(ArtistEntity::fromModel).toList())
            .createdAt(idol.getCreatedAt())
            .updatedAt(idol.getUpdatedAt())
            .deletedAt(idol.getDeletedAt())
            .build();
    }

    public Idol toModel() {
        return Idol.builder()
            .id(id)
            .name(name)
            .artists(artistEntities.stream().map(ArtistEntity::toDomain).toList())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
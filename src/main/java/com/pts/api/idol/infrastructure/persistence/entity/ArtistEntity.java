package com.pts.api.idol.infrastructure.persistence.entity;

import com.pts.api.idol.domain.model.Artist;
import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artists", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idol_id", "name"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private IdolEntity idolEntity;

    @Column(name = "name", nullable = false)
    private String name;

    @Builder
    public ArtistEntity(Long id, IdolEntity idolEntity, String name,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.idolEntity = idolEntity;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Artist toDomain() {
        return Artist.builder()
            .id(id)
            .name(name)
            .idol(idolEntity.toModel())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static ArtistEntity fromModel(Artist artist) {
        return ArtistEntity.builder()
            .id(artist.getId())
            .idolEntity(IdolEntity.fromModel(artist.getIdol()))
            .name(artist.getName())
            .createdAt(artist.getCreatedAt())
            .updatedAt(artist.getUpdatedAt())
            .deletedAt(artist.getDeletedAt())
            .build();
    }

    public Artist toModel() {
        return Artist.builder()
            .id(id)
            .name(name)
            .idol(idolEntity.toModel())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
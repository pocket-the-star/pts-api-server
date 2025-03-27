package com.pts.api.feed.infrastructure.persistence.entity;

import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
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
@Table(name = "feed_images")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "feed_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private FeedEntity feed;

    @Builder
    public FeedImageEntity(Long id, String url, FeedEntity feed, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.url = url;
        this.feed = feed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static FeedImageEntity fromModel(FeedImage feedImage) {
        return FeedImageEntity.builder()
            .id(feedImage.getId())
            .url(feedImage.getUrl())
            .createdAt(feedImage.getCreatedAt())
            .updatedAt(feedImage.getUpdatedAt())
            .deletedAt(feedImage.getDeletedAt())
            .build();
    }

    public FeedImage toModel() {
        return FeedImage.builder()
            .id(this.id)
            .url(this.url)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .deletedAt(this.deletedAt)
            .build();
    }
}

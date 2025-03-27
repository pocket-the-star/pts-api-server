package com.pts.api.feed.infrastructure.persistence.entity;

import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feeds")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImageEntity> feedImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private ProductGrade grade;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeedStatus status;

    @Builder
    public FeedEntity(Long id, Long userId, Long productId, String content,
        List<FeedImageEntity> feedImages,
        ProductGrade grade,
        Integer price, Integer quantity, FeedStatus status, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.feedImages = feedImages;
        this.grade = grade;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static FeedEntity fromModel(Feed feed) {
        List<FeedImageEntity> feedImageEntities = feed.getFeedImages().stream()
            .map(FeedImageEntity::fromModel)
            .collect(Collectors.toList());

        FeedEntity feedEntity = FeedEntity.builder()
            .id(feed.getId())
            .userId(feed.getUserId())
            .productId(feed.getProductId())
            .content(feed.getContent())
            .feedImages(feedImageEntities)
            .grade(feed.getGrade())
            .price(feed.getPrice())
            .quantity(feed.getQuantity())
            .status(feed.getStatus())
            .createdAt(feed.getCreatedAt())
            .updatedAt(feed.getUpdatedAt())
            .deletedAt(feed.getDeletedAt())
            .build();

        feedImageEntities.forEach(feedImageEntity -> feedImageEntity.setFeed(feedEntity));

        return feedEntity;
    }

    public Feed toModel() {
        List<FeedImage> feedImages = this.feedImages.stream()
            .map(FeedImageEntity::toModel)
            .toList();

        return Feed.builder()
            .id(this.id)
            .userId(this.userId)
            .productId(this.productId)
            .content(this.content)
            .feedImages(feedImages)
            .grade(this.grade)
            .price(this.price)
            .quantity(this.quantity)
            .status(this.status)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .deletedAt(this.deletedAt)
            .build();
    }
} 
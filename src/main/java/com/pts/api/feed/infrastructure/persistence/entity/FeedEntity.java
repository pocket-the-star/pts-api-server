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

    public void addFeedImage(FeedImageEntity feedImage) {
        feedImages.add(feedImage);
        feedImage.setFeed(this);
    }

    public static FeedEntity fromModel(Feed feed) {
        FeedEntity feedEntity = FeedEntity.builder()
            .id(feed.getId())
            .userId(feed.getUserId())
            .productId(feed.getProductId())
            .content(feed.getContent())
            .grade(feed.getGrade())
            .price(feed.getPrice())
            .quantity(feed.getQuantity())
            .status(feed.getStatus())
            .createdAt(feed.getCreatedAt())
            .updatedAt(feed.getUpdatedAt())
            .deletedAt(feed.getDeletedAt())
            .build();

        feed.getFeedImages().forEach(feedImage -> {
            FeedImageEntity feedImageEntity = FeedImageEntity.fromModel(feedImage);
            feedEntity.addFeedImage(feedImageEntity);
        });

        return feedEntity;
    }

    public Feed toModel() {
        return Feed.builder()
            .id(id)
            .userId(userId)
            .productId(productId)
            .content(content)
            .feedImages(feedImages.stream()
                .map(FeedImageEntity::toModel)
                .collect(Collectors.toList()))
            .grade(grade)
            .price(price)
            .quantity(quantity)
            .status(status)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
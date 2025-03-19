package com.pts.api.feed.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
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
import java.util.List;
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
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "feed")
    private List<FeedImage> feedImages;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private FeedType type;

    @Enumerated(EnumType.STRING)
    private ProductGrade grade;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeedStatus status;

    @Builder
    public Feed(Long id, Long userId, Long productId, String content, List<FeedImage> feedImages,
        FeedType feedType, ProductGrade grade,
        Integer price, Integer quantity, FeedStatus status, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.feedImages = feedImages;
        this.type = feedType;
        this.grade = grade;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}

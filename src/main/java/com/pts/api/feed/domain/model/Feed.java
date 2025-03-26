package com.pts.api.feed.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Feed extends BaseModel {

    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private List<FeedImage> feedImages;
    private ProductGrade grade;
    private Integer price;
    private Integer quantity;
    private FeedStatus status;

    public void update(String content, List<FeedImage> feedImages, ProductGrade grade,
        Integer price, Integer quantity, LocalDateTime updatedAt) {
        this.content = content;
        this.feedImages = feedImages;
        this.grade = grade;
        this.price = price;
        this.quantity = quantity;
        this.markUpdated(updatedAt);
    }

    public void delete(LocalDateTime deletedAt) {
        this.markDeleted(deletedAt);
    }

    public void decreaseStock(Integer quantity, LocalDateTime updatedAt) {
        if (this.quantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.quantity -= quantity;
        markUpdated(updatedAt);
    }

    public void restoreStock(Integer quantity, LocalDateTime updatedAt) {
        this.quantity += quantity;
        markUpdated(updatedAt);
    }
} 
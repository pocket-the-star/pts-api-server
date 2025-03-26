package com.pts.api.feed.application.dto.response;

import com.pts.api.feed.domain.model.Feed;
import com.pts.api.feed.domain.model.FeedImage;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;

public record FeedResponse(
    Long id,
    Long userId,
    Long productId,
    String content,
    List<String> imageUrls,
    ProductGrade grade,
    Integer price,
    Integer quantity,
    FeedStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static FeedResponse from(Feed feed) {
        return new FeedResponse(
            feed.getId(),
            feed.getUserId(),
            feed.getProductId(),
            feed.getContent(),
            feed.getFeedImages().stream()
                .map(FeedImage::getUrl)
                .toList(),
            feed.getGrade(),
            feed.getPrice(),
            feed.getQuantity(),
            feed.getStatus(),
            feed.getCreatedAt(),
            feed.getUpdatedAt()
        );
    }
}

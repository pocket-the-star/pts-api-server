package com.pts.api.feed.dto.response;

import com.pts.api.feed.model.Feed;
import com.pts.api.feed.model.FeedImage;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;

public record ReadFeedResponseDto(
    Long id,
    Long userId,
    Long productId,
    String content,
    List<String> imageUrl,
    FeedType type,
    ProductGrade grade,
    Integer price,
    Integer quantity,
    FeedStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReadFeedResponseDto fromFeed(Feed feed) {
        return new ReadFeedResponseDto(
            feed.getId(),
            feed.getUserId(),
            feed.getProductId(),
            feed.getContent(),
            feed.getFeedImages().stream()
                .map(FeedImage::getImageUrl)
                .toList(),
            feed.getType(),
            feed.getGrade(),
            feed.getPrice(),
            feed.getQuantity(),
            feed.getStatus(),
            feed.getCreatedAt(),
            feed.getUpdatedAt()
        );
    }
}

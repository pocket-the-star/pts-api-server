package com.pts.api.feed.dto.request;

import com.pts.api.feed.model.FeedImage;
import java.time.LocalDateTime;

public record CreateFeedImageRequestDto(
    String imageUrl
) {

    FeedImage toFeedImages(Integer sortOrder, LocalDateTime now) {
        return FeedImage.builder()
            .imageUrl(imageUrl)
            .sortOrder(sortOrder)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }
}

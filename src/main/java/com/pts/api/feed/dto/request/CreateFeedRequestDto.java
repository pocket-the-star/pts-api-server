package com.pts.api.feed.dto.request;

import com.pts.api.feed.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import lombok.NonNull;

public record CreateFeedRequestDto(
    @NonNull
    Long productId,
    @NonNull
    String content,
    @NonNull
    List<CreateFeedImageRequestDto> feedImages,
    @NonNull
    FeedType feedType,
    @NonNull
    ProductGrade grade,
    @NonNull
    Integer price,
    @NonNull
    Integer quantity
) {

    public Feed toFeed(Long userId, LocalDateTime now) {
        Feed feed = Feed.builder()
            .userId(userId)
            .productId(productId)
            .content(content)
            .feedImages(
                IntStream.range(0, feedImages.size())
                    .mapToObj(i -> feedImages.get(i).toFeedImages(i, now))
                    .toList()
            )
            .feedType(feedType)
            .grade(grade)
            .price(price)
            .quantity(quantity)
            .status(FeedStatus.PENDING)
            .createdAt(now)
            .updatedAt(now)
            .build();

        feed.getFeedImages().forEach(image -> image.setFeed(feed));

        return feed;
    }
}

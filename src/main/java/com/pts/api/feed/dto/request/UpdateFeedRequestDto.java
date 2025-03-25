package com.pts.api.feed.dto.request;

import com.pts.api.feed.model.Feed;
import java.time.LocalDateTime;
import lombok.NonNull;

public record UpdateFeedRequestDto(
    @NonNull
    String content,
    @NonNull
    Integer price,
    @NonNull
    Integer quantity
) {

    public Feed toFeed(Feed feed, LocalDateTime now) {
        feed.setContent(content);
        feed.setPrice(price);
        feed.setQuantity(quantity);
        feed.setUpdatedAt(now);

        return feed;
    }
}

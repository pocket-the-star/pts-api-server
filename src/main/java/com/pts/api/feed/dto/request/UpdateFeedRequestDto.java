package com.pts.api.feed.dto.request;

import lombok.NonNull;

public record UpdateFeedRequestDto(
    @NonNull
    String content,
    @NonNull
    Integer price,
    @NonNull
    Integer quantity
) {

}

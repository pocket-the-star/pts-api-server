package com.pts.api.feed.dto.request;

import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.util.List;
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

}

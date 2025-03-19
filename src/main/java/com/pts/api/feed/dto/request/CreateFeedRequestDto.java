package com.pts.api.feed.dto.request;

import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.util.List;

public record CreateFeedRequestDto(
    Long userId,
    Long productId,
    String content,
    List<CreateFeedImageRequestDto> feedImages,
    FeedType feedType,
    ProductGrade grade,
    Integer price,
    Integer quantity
) {

}

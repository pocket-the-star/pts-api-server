package com.pts.api.feed.application.dto.request;

import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.util.List;

public record CreateFeedRequest(
    Long productId,
    String content,
    List<String> imageUrls,
    ProductGrade grade,
    Integer price,
    Integer quantity
) {
} 
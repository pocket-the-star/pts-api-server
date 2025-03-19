package com.pts.api.lib.internal.shared.event.data;

import com.pts.api.lib.internal.shared.enums.FeedType;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import com.pts.api.lib.internal.shared.event.EventData;

public record FeedCreateData(Long userId, Long productId, String imageUrl, FeedType feedType,
                             ProductGrade grade,
                             Integer price, Integer quantity) implements
    EventData {

}

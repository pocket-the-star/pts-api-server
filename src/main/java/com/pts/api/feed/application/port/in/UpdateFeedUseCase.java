package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.domain.model.Feed;

public interface UpdateFeedUseCase {
    Feed update(Long userId, Long id, UpdateFeedRequest request);
} 
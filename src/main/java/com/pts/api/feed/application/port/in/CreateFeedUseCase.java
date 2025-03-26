package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.domain.model.Feed;

public interface CreateFeedUseCase {
    Feed create(Long userId, CreateFeedRequest request);
} 
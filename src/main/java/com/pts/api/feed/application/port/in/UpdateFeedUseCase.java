package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.request.UpdateFeedRequest;

public interface UpdateFeedUseCase {

    void update(Long userId, Long id, UpdateFeedRequest request);
} 
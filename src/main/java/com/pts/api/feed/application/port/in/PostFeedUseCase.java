package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.request.CreateFeedRequest;

public interface PostFeedUseCase {

    void create(Long userId, CreateFeedRequest request);
} 
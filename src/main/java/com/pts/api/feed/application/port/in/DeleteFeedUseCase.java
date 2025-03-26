package com.pts.api.feed.application.port.in;

public interface DeleteFeedUseCase {
    void delete(Long userId, Long id);
} 
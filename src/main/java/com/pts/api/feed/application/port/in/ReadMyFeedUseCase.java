package com.pts.api.feed.application.port.in;

import com.pts.api.feed.domain.model.Feed;
import java.util.List;

public interface ReadMyFeedUseCase {

    List<Feed> findByUserId(Long userId, Long lastFeedId, Integer limit);
} 
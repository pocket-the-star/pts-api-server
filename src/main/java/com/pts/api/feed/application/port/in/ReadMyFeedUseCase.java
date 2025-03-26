package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.response.MyFeedResponse;
import java.util.List;

public interface ReadMyFeedUseCase {

    List<MyFeedResponse> findByUserId(Long userId, Long lastFeedId, Integer limit);
} 
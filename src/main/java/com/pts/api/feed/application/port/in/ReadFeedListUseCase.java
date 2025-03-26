package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.response.FeedResponse;
import java.util.List;

public interface ReadFeedListUseCase {

    List<FeedResponse> findAll(Long lastId, Integer limit);
} 
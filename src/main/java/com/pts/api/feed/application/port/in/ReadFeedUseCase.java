package com.pts.api.feed.application.port.in;

import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import java.util.List;

public interface ReadFeedUseCase {

    List<MyFeedResponse> getMyFeeds(Long userId, Long lastFeedId, Integer limit);

    List<FeedResponse> getFeeds(Long lastId, Integer limit);
} 
package com.pts.api.feed.application.port.in;

import com.pts.api.feed.domain.model.Feed;
import java.util.List;

public interface ReadFeedListUseCase {

    List<Feed> findAll(Long lastId, Integer limit);
} 
package com.pts.api.feed.application.port.out;

import com.pts.api.feed.domain.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import java.util.List;
import java.util.Optional;

public interface FeedRepositoryPort {

    Feed save(Feed feed);

    Optional<Feed> findByUserIdAndProductIdAndFeedStatusAndFeedType(
        Long userId,
        Long ProductId,
        FeedStatus status);

    List<Feed> findByUserIdAndDeletedAtIsNull(Long userId, Long offset, Integer limit);

    List<Feed> findByDeletedAtIsNull(Long offset, Integer limit);

    Optional<Feed> findOneById(Long id);
} 
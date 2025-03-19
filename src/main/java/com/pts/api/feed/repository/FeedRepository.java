package com.pts.api.feed.repository;

import com.pts.api.feed.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.FeedType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("""
        SELECT f
        FROM Feed f
        WHERE f.userId = :userId
            AND f.productId = :productId
            AND f.status = :status
            AND f.type = :feedType""")
    Optional<Feed> findByUserIdAndProductIdAndFeedStatusAndFeedType(Long userId, Long ProductId,
        FeedStatus status, FeedType feedType);
}

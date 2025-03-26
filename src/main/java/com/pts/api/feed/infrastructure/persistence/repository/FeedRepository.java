package com.pts.api.feed.infrastructure.persistence.repository;

import com.pts.api.feed.infrastructure.persistence.model.FeedEntity;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedRepository extends JpaRepository<FeedEntity, Long> {

    @Query("""
        SELECT f
        FROM FeedEntity f
        WHERE f.userId = :userId
            AND f.productId = :productId
            AND f.status = :status""")
    Optional<FeedEntity> findByUserIdAndProductIdAndFeedStatusAndFeedType(Long userId,
        Long ProductId,
        FeedStatus status);


    @Query(
        value = """
            select *
            from feeds
            where feeds.user_id = :userId
                and feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeedEntity> findByUserIdAndDeletedAtIsNull(Long userId, Long offset, Integer limit);

    @Query(
        value = """
            select *
            from feeds
            where feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<FeedEntity> findByDeletedAtIsNull(Long offset, Integer limit);

    @Query(
        value = """
            select *
            from feeds
            where feeds.id = :id
                and feeds.deleted_at is null
            order by feeds.id desc
            limit 1
            """, nativeQuery = true)
    Optional<FeedEntity> findOneById(Long id);
}

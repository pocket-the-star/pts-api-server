package com.pts.api.feed.infrastructure.persistence.repository;

import com.pts.api.feed.infrastructure.persistence.entity.FeedImageEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedImageRepository extends JpaRepository<FeedImageEntity, Long> {

    @Query(
        value = """
            select *
            from feed_images
            where feed_images.feed_id = :feedId
                and feed_images.deleted_at is null
            order by feed_images.id desc
            """,
        nativeQuery = true
    )
    List<FeedImageEntity> findAll(Long feedId);

    @Query(
        value = """
            select *
            from feed_images
            where feed_images.id = :id
                and feed_images.deleted_at is null
            limit 1
            """,
        nativeQuery = true
    )
    Optional<FeedImageEntity> findOneById(Long id);

    @Query(
        value = """
            select *
            from feed_images
            where feed_images.feed_id = :feedId
                and feed_images.deleted_at is null
            """,
        nativeQuery = true
    )
    List<FeedImageEntity> findByFeedId(Long feedId);
}

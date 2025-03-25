package com.pts.api.feed.repository;

import com.pts.api.feed.model.FeedImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

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
    List<FeedImage> findAll(Long feedId);

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
    Optional<FeedImage> findOneById(Long id);

    @Query(
        value = """
            select *
            from feed_images
            where feed_images.feed_id = :feedId
                and feed_images.deleted_at is null
            """,
        nativeQuery = true
    )
    List<FeedImage> findByFeedId(Long feedId);
}

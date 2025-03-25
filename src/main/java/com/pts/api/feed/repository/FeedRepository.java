package com.pts.api.feed.repository;

import com.pts.api.feed.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.FeedType;
import java.util.List;
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


    @Query(
        value = """
            select *
            from feeds
            where feeds.user_id = :userId
                and feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<Feed> findByUserIdAndDeletedAtIsNull(Long userId, Long offset, Integer limit);

    @Query(
        value = """
            select *
            from feeds
            where feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<Feed> findByDeletedAtIsNull(Long offset, Integer limit);

    @Query(
        value = """
            select feeds.*
            from feeds
            inner join product as p
                on feeds.product_id = p.id
            inner join artists as a
                on p.artist_id = a.id
            inner join idols as i
                on a.idol_id = i.id
            where i.id = :IdolId
                and feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<Feed> findByDeletedAtIsNull(Long IdolId, Long offset, Integer limit);

    @Query(
        value = """
            select feeds.*
            from feeds
            inner join product as p
                on feeds.product_id = p.id
            inner join sub_categories as sc
                on p.sub_category_id = sc.id
            inner join categories as c
                on sc.category_id = c.id
            inner join artists as a
                on p.artist_id = a.id
            inner join idols as i
                on a.idol_id = i.id
            where i.id = :IdolId
                and c.id = :categoryId
                and feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<Feed> findByDeletedAtIsNull(Long IdolId, Long categoryId, Long offset,
        Integer limit);


    @Query(
        value = """
            select feeds.*
            from feeds
            inner join product as p
                on feeds.product_id = p.id
            inner join sub_categories as sc
                on p.sub_category_id = sc.id
            inner join categories as c
                on sc.category_id = c.id
            inner join artists as a
                on p.artist_id = a.id
            inner join idols as i
                on a.idol_id = i.id
            where i.id = :IdolId
                and c.id = :categoryId
                and sc.id = :subCategoryID
                and feeds.deleted_at is null
            order by feeds.id desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<Feed> findByDeletedAtIsNull(Long IdolId, Long categoryId, Long subCategoryID,
        Long offset, Integer limit);
}

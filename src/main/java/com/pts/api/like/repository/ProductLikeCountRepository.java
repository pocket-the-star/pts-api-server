package com.pts.api.like.repository;

import com.pts.api.like.model.ProductLikeCount;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductLikeCountRepository extends JpaRepository<ProductLikeCount, Long> {

    @Query(
        value = """
            update product_like_counts
            set count = count + 1
            where product_id = :productId
            """,
        nativeQuery = true
    )
    @Modifying
    void increase(@Param("productId") Long productId);

    @Query(
        value = """
            update product_like_counts
            set count = count - 1
            where product_id = :productId
            """,
        nativeQuery = true
    )
    @Modifying
    void decrease(@Param("productId") Long productId);

    @Query(
        value = """
            select *
            from product_like_counts
            where product_id = :productId
            """,
        nativeQuery = true
    )
    Optional<ProductLikeCount> findOneByProductId(@Param("productId") Long productId);
}

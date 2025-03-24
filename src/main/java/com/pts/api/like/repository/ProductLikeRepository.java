package com.pts.api.like.repository;

import com.pts.api.like.model.ProductLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    Optional<ProductLike> findOneByProductIdAndUserId(Long productId, Long userId);
}

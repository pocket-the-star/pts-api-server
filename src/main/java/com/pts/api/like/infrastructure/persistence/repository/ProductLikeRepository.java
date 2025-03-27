package com.pts.api.like.infrastructure.persistence.repository;

import com.pts.api.like.infrastructure.persistence.entity.ProductLikeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLikeEntity, Long> {

    Optional<ProductLikeEntity> findOneByProductIdAndUserId(Long productId, Long userId);
}

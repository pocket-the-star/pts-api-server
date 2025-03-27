package com.pts.api.like.application.port.out;

import com.pts.api.like.domain.model.ProductLike;
import java.util.Optional;

public interface ProductLikeRepositoryPort {

    ProductLike save(ProductLike productLike);

    Optional<ProductLike> findByProductIdAndUserId(Long productId, Long userId);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    void delete(ProductLike productLike);
} 
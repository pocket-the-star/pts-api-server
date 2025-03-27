package com.pts.api.like.application.port.out;

import com.pts.api.like.domain.model.ProductLikeCount;
import java.util.Optional;

public interface ProductLikeCountRepositoryPort {

    ProductLikeCount save(ProductLikeCount productLikeCount);

    Optional<ProductLikeCount> findByProductId(Long productId);

    boolean existsByProductId(Long productId);
} 
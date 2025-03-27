package com.pts.api.like.application.port.in;

import com.pts.api.like.domain.model.ProductLikeCount;

public interface ProductLikeCountUseCase {

    ProductLikeCount getCount(Long productId);

    void increase(Long productId);

    void decrease(Long productId);

    void create(Long productId);
} 
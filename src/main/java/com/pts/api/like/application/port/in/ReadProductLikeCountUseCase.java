package com.pts.api.like.application.port.in;

import com.pts.api.like.domain.model.ProductLikeCount;

public interface ReadProductLikeCountUseCase {

    ProductLikeCount getCount(Long productId);
} 
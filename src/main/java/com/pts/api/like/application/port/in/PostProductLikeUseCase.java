package com.pts.api.like.application.port.in;

public interface PostProductLikeUseCase {

    void like(Long productId, Long userId);
} 
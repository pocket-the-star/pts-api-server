package com.pts.api.like.application.port.in;

public interface ProductLikeUseCase {

    void like(Long productId, Long userId);

    void unlike(Long productId, Long userId);

    boolean isLiked(Long productId, Long userId);
} 
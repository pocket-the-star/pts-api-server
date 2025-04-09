package com.pts.api.like.application.port.in;

public interface DeleteProductLikeUseCase {

    void unlike(Long productId, Long userId);
}

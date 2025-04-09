package com.pts.api.like.application.port.in;

public interface ReadProductLikeUseCase {

    boolean isLiked(Long productId, Long userId);

}

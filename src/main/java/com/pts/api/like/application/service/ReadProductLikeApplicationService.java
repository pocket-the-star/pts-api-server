package com.pts.api.like.application.service;

import com.pts.api.like.application.port.in.ReadProductLikeUseCase;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadProductLikeApplicationService implements ReadProductLikeUseCase {

    private final ProductLikeRepositoryPort productLikeRepository;

    @Override
    public boolean isLiked(Long productId, Long userId) {
        return productLikeRepository.existsByProductIdAndUserId(productId, userId);
    }
}

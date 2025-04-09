package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.application.port.in.ReadProductLikeCountUseCase;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadProductLikeCountService implements ReadProductLikeCountUseCase {

    private final ProductLikeCountRepositoryPort productLikeCountRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductLikeCount getCount(Long productId) {
        return productLikeCountRepository.findByProductId(productId)
            .orElseThrow(() -> new NotFoundException("좋아요 카운트가 존재하지 않습니다.: " + productId));
    }

}

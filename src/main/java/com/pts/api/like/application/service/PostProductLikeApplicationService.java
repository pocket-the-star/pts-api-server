package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.like.application.port.in.PostProductLikeUseCase;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostProductLikeApplicationService implements PostProductLikeUseCase {

    private final ProductLikeRepositoryPort productLikeRepository;
    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort outboxPublisher;

    @Override
    @Transactional
    public void like(Long productId, Long userId) {
        productRepository.findById(productId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 상품입니다.: " + productId));

        productLikeRepository.findByProductIdAndUserId(productId, userId).ifPresent(
            productLikeEntity -> {
                throw new AlreadyExistException("이미 좋아요한 상품입니다.: " + productId);
            });

        productLikeRepository.save(ProductLike.builder()
            .productId(productId)
            .userId(userId)
            .build());

        outboxPublisher.publish(EventType.PRODUCT_LIKE, new ProductLikeData(productId));
    }
}

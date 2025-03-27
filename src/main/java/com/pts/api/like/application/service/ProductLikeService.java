package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.in.ProductLikeUseCase;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService implements ProductLikeUseCase {

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

    @Override
    @Transactional
    public void unlike(Long productId, Long userId) {
        ProductLike productLike = productLikeRepository.findByProductIdAndUserId(productId,
                userId)
            .orElseThrow(() -> new NotFoundException("좋아요하지 않은 상품입니다.: " + productId));

        productLikeRepository.delete(productLike);

        outboxPublisher.publish(EventType.PRODUCT_UNLIKE, new ProductUnLikeData(productId));
    }

    @Override
    public boolean isLiked(Long productId, Long userId) {
        return productLikeRepository.existsByProductIdAndUserId(productId, userId);
    }
}

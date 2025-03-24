package com.pts.api.like.service;

import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.model.ProductLike;
import com.pts.api.like.repository.ProductLikeRepository;
import com.pts.api.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {

    private final ProductLikeRepository productLikeRepository;
    private final ProductRepository productRepository;
    private final OutboxPublisher outboxPublisher;

    @Transactional
    public void like(Long productId, Long userId) {
        productRepository.findById(productId).orElseThrow(
            () -> new NotFoundException("존재하지 않는 상품입니다.: " + productId));

        productLikeRepository.findOneByProductIdAndUserId(productId, userId).ifPresent(
            productLike -> {
                throw new AlreadyExistException("이미 좋아요한 상품입니다.: " + productId);
            });

        productLikeRepository.save(ProductLike.builder()
            .productId(productId)
            .userId(userId)
            .build());

        outboxPublisher.publish(EventType.PRODUCT_LIKE, new ProductLikeData(productId));
    }

    @Transactional
    public void unlike(Long productId, Long userId) {
        ProductLike productLike = productLikeRepository.findOneByProductIdAndUserId(productId,
                userId)
            .orElseThrow(() -> new NotFoundException("좋아요하지 않은 상품입니다.: " + productId));

        productLikeRepository.delete(productLike);

        outboxPublisher.publish(EventType.PRODUCT_UNLIKE, new ProductUnLikeData(productId));
    }
}

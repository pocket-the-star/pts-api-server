package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.in.DeleteProductLikeUseCase;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProductLikeApplicationService implements DeleteProductLikeUseCase {

    private final ProductLikeRepositoryPort productLikeRepository;
    private final EventPublisherPort outboxPublisher;

    @Override
    @Transactional
    public void unlike(Long productId, Long userId) {
        ProductLike productLike = productLikeRepository.findByProductIdAndUserId(productId,
                userId)
            .orElseThrow(() -> new NotFoundException("좋아요하지 않은 상품입니다.: " + productId));

        productLikeRepository.delete(productLike);

        outboxPublisher.publish(EventType.PRODUCT_UNLIKE, new ProductUnLikeData(productId));
    }
}

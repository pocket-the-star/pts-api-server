package com.pts.api.like.application.service.handler;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DecreaseProductLikeCountHandler implements
    ProductLikeCountEventHandler<ProductUnLikeData> {

    private static final EventType EVENT_TYPE = EventType.PRODUCT_UNLIKE;
    private final ProductLikeCountRepositoryPort productLikeCountRepository;
    private final LockRepository productLikeLockRepository;

    /**
     * 좋아요 카운트 감소 이벤트를 처리합니다.
     *
     * @param event 좋아요 카운트 감소 이벤트
     */
    @Override
    @Transactional
    public void handle(Event<ProductUnLikeData> event) {
        Long productId = event.getData().productId();

        lock(productId, () -> {
            productLikeCountRepository.findByProductId(productId).ifPresentOrElse(
                productLikeCount -> {
                    productLikeCount.decrement();
                    productLikeCountRepository.save(productLikeCount);
                }, () -> {
                    throw new NotFoundException("좋아요 카운트가 존재하지 않습니다.: " + productId);
                });
        });
    }

    private void lock(Long productId, Runnable action) {
        int LOCK_EXPIRATION = 1;
        RLock lock = productLikeLockRepository.getFairLock(productId);
        try {
            lock.lock(LOCK_EXPIRATION, TimeUnit.SECONDS);

            action.run();

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public boolean supports(Event<ProductUnLikeData> event) {
        return event.getType().equals(EVENT_TYPE);
    }
}

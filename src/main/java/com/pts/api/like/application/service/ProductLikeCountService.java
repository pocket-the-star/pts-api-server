package com.pts.api.like.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.like.application.port.in.ProductLikeCountUseCase;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeCountService implements ProductLikeCountUseCase {

    private final ProductLikeCountRepositoryPort productLikeCountRepository;
    private final LockRepository productLikeLockRepository;

    @Override
    @Transactional
    public void create(Long productId) {
        productLikeCountRepository.save(
            ProductLikeCount.builder().productId(productId).count(0L).build());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductLikeCount getCount(Long productId) {
        return productLikeCountRepository.findByProductId(productId)
            .orElseThrow(() -> new NotFoundException("좋아요 카운트가 존재하지 않습니다.: " + productId));
    }

    @Override
    @Transactional
    public void increase(Long productId) {
        lock(productId, () -> {
            productLikeCountRepository.findByProductId(productId)
                .ifPresentOrElse(productLikeCount -> {
                    productLikeCount.increment();
                    productLikeCountRepository.save(productLikeCount);
                }, () -> productLikeCountRepository.save(
                    ProductLikeCount.builder().productId(productId).count(1L).build()));
        });
    }

    @Override
    public void decrease(Long productId) {
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
        int LOCK_EXPIRATION = 2;
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
}

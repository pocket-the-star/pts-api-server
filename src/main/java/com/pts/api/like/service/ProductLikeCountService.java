package com.pts.api.like.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.model.ProductLikeCount;
import com.pts.api.like.repository.ProductLikeCountRepository;
import com.pts.api.like.repository.ProductLikeLockRepository;
import jakarta.transaction.Transactional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductLikeCountService {

    private final ProductLikeCountRepository productLikeCountRepository;
    private final ProductLikeLockRepository productLikeLockRepository;

    public void create(Long productId) {
        productLikeCountRepository.save(ProductLikeCount.builder()
            .productId(productId)
            .count(0L)
            .build());
    }

    @Transactional
    public void increase(Long productId) {
        lock(productId, () -> {
            productLikeCountRepository.findOneByProductId(productId).ifPresentOrElse(
                productLikeCount -> productLikeCountRepository.increase(productId),
                () -> productLikeCountRepository.save(ProductLikeCount.builder()
                    .productId(productId)
                    .count(1L)
                    .build()));

        });
    }

    public void decrease(Long productId) {
        lock(productId, () -> {
            productLikeCountRepository.findOneByProductId(productId).ifPresentOrElse(
                productLikeCount -> productLikeCountRepository.decrease(productId),
                () -> {
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

        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

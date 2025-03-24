package com.pts.api.like.service;

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

    @Transactional
    public void increase(Long productId) {
        lock(productId, () -> {
            productLikeCountRepository.increase(productId);
        });
    }

    @Transactional
    public void decrease(Long productId) {
        lock(productId, () -> {
            productLikeCountRepository.decrease(productId);
        });
    }


    private void lock(Long productId, Runnable action) {
        int ROCK_TIMEOUT = 5;
        int LOCK_EXPIRATION = 10;
        RLock lock = productLikeLockRepository.getFairLock(productId);

        try {
            if (lock.tryLock(ROCK_TIMEOUT, LOCK_EXPIRATION, TimeUnit.SECONDS)) {
                action.run();
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

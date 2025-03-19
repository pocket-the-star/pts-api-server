package com.pts.api.product.service;

import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.product.repository.ProductLockRepository;
import com.pts.api.product.repository.ProductRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductLockRepository productLockService;

    @Transactional
    public void priceUpdate(FeedCreateData data) {
        executeWithLock(data.productId(), () ->
            productRepository.findById(data.productId())
                .ifPresent(product -> {
                    product.priceUpdate(data.price());
                    productRepository.save(product);
                }));
    }

    public void executeWithLock(Long key, Runnable action) {
        RLock lock = productLockService.getFairLock(key);
        int ROCK_TIMEOUT = 5;
        int LOCK_EXPIRATION = 10;

        try {
            if (lock.tryLock(ROCK_TIMEOUT, LOCK_EXPIRATION, TimeUnit.SECONDS)) {
                action.run();
            }
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            }

            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

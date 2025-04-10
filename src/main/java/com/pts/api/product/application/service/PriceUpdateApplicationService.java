package com.pts.api.product.application.service;

import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.port.in.PriceUpdateUseCase;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriceUpdateApplicationService implements PriceUpdateUseCase {

    private final ProductRepositoryPort productRepository;
    private final LockRepository productLockRepository;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 상품 가격 업데이트
     *
     * @param productId 상품 ID
     * @param price     가격
     */
    @Override
    @Transactional
    public void updatePrice(Long productId, Integer price) {
        executeWithLock(productId, () ->
            productRepository.findById(productId)
                .ifPresent(product -> {
                    product.priceUpdate(price, dateTimeUtil.now());
                    productRepository.save(product);
                }));
    }

    private void executeWithLock(Long key, Runnable action) {
        RLock lock = productLockRepository.getFairLock(key);
        int LOCK_EXPIRATION = 2;

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
package com.pts.api.product.service;

import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.dto.request.CreateProductRequestDto;
import com.pts.api.product.model.Product;
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
    private final OutboxPublisher outboxPublisher;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public Product create(CreateProductRequestDto dto) {
        Product newProduct = productRepository.save(dto.toProduct(dateTimeUtil.now()));

        outboxPublisher.publish(
            EventType.PRODUCT_CREATE,
            new ProductCreateData(newProduct.getId())
        );

        return newProduct;
    }

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

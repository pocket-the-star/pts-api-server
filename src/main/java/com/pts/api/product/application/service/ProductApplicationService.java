package com.pts.api.product.application.service;

import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.dto.response.ProductResponse;
import com.pts.api.product.application.port.in.CreateProductUseCase;
import com.pts.api.product.application.port.in.PriceUpdateUseCase;
import com.pts.api.product.application.port.in.ReadProductListUseCase;
import com.pts.api.product.application.port.in.ReadProductUseCase;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.common.exception.ProductNotFoundException;
import com.pts.api.product.domain.model.Product;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductApplicationService implements CreateProductUseCase, ReadProductUseCase,
    ReadProductListUseCase, PriceUpdateUseCase {

    private final ProductRepositoryPort productRepository;
    private final LockRepository productLockRepository;
    private final EventPublisherPort eventPublisher;
    private final DateTimeUtil dateTimeUtil;

    @Override
    @Transactional
    public void create(CreateProductRequest request) {
        Product newProduct = request.toProduct(dateTimeUtil.now());
        Product savedProduct = productRepository.save(newProduct);

        eventPublisher.publish(
            EventType.PRODUCT_CREATE,
            new ProductCreateData(savedProduct.getId())
        );
    }

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

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
            .map(ProductResponse::from)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductResponse> findAll(Long idolId, Long categoryId, Long subCategoryId,
        Long offset, int limit) {
        return productRepository.findAll(idolId, categoryId, subCategoryId, offset, limit)
            .stream()
            .map(ProductResponse::from)
            .toList();
    }
}
package com.pts.api.product.application.service;

import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.port.in.PostProductUseCase;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostProductApplicationService implements PostProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort eventPublisher;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 상품 생성
     *
     * @param request 상품 생성 요청
     */
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
}
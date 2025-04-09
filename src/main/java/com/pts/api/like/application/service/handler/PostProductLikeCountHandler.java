package com.pts.api.like.application.service.handler;

import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostProductLikeCountHandler implements
    ProductLikeCountEventHandler<ProductCreateData> {

    private static final EventType EVENT_TYPE = EventType.PRODUCT_CREATE;
    private final ProductLikeCountRepositoryPort productLikeCountRepository;
    private final LockRepository productLikeLockRepository;

    /**
     * 상품 생성 이벤트를 처리합니다.
     *
     * @param event 상품 생성 이벤트
     */
    @Override
    @Transactional
    public void handle(Event<ProductCreateData> event) {
        Long INITIAL_COUNT = 0L;
        Long productId = event.getData().productId();

        productLikeCountRepository.save(
            ProductLikeCount.builder().productId(productId).count(INITIAL_COUNT).build());
    }

    @Override
    public boolean supports(Event<ProductCreateData> event) {
        return event.getType().equals(EVENT_TYPE);
    }
}

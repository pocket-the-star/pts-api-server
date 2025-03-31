package com.pts.api.like.presentation.consumer;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType.Topic;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.in.ProductLikeCountUseCase;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductLikeCountConsumer {

    private final ProductLikeCountUseCase productLikeCountUseCase;

    @KafkaListener(topics = {
        Topic.PRODUCT_LIKE,
        Topic.PRODUCT_UNLIKE,
        Topic.PRODUCT_CREATE
    }, groupId = "product-like-count")
    public void consume(String message) {
        try {
            Event<EventData> event = Event.fromJson(message);
            handleEvent(Objects.requireNonNull(event));

            log.info("좋아요 이벤트 메세지 처리 완료: {}", message);
        } catch (Exception e) {

            log.error("좋아요 이벤트 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }

    private void handleEvent(Event<EventData> event) {
        switch (event.getType()) {
            case PRODUCT_LIKE:
                productLikeCountUseCase.increase(((ProductLikeData) event.getData()).productId());
                break;
            case PRODUCT_UNLIKE:
                productLikeCountUseCase.decrease(((ProductUnLikeData) event.getData()).productId());
                break;
            case PRODUCT_CREATE:
                productLikeCountUseCase.create(((ProductCreateData) event.getData()).productId());
                break;
            default:
                throw new NotFoundException("존재하지 않는 이벤트 타입입니다.: " + event.getType());
        }
    }
}

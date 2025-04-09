package com.pts.api.like.presentation.consumer;

import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType.Topic;
import com.pts.api.like.application.port.in.ProductLikeCountHandleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductLikeCountConsumer {

    private final ProductLikeCountHandleUseCase productLikeCountHandleUseCase;

    @KafkaListener(topics = {
        Topic.PRODUCT_LIKE,
        Topic.PRODUCT_UNLIKE,
        Topic.PRODUCT_CREATE
    }, groupId = "product-like-count")
    public void consume(String message) {
        try {
            Event<EventData> event = Event.fromJson(message);

            productLikeCountHandleUseCase.handleEvent(event);

            log.info("상품 좋아요 이벤트 메세지 처리 완료: {}", message);
        } catch (Exception e) {

            log.error("상품 좋아요 이벤트 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }
}

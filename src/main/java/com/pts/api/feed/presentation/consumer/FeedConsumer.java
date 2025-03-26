package com.pts.api.feed.presentation.consumer;

import com.pts.api.feed.application.port.in.DecreaseStockUseCase;
import com.pts.api.feed.application.port.in.RestoreStockUseCase;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType.Topic;
import com.pts.api.lib.internal.shared.event.data.OrderCancelData;
import com.pts.api.lib.internal.shared.event.data.OrderCreateData;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedConsumer {

    private final DecreaseStockUseCase decreaseStockUseCase;
    private final RestoreStockUseCase restoreStockUseCase;

    @KafkaListener(topics = {
        Topic.ORDER_CREATED,
        Topic.ORDER_CANCELLED
    }, groupId = "feed-service")
    public void consume(String message) {
        try {
            Event<EventData> event = Event.fromJson(message);
            handleEvent(Objects.requireNonNull(event));

            log.info("주문 이벤트 메시지 처리 완료: {}", message);
        } catch (Exception e) {
            log.error("주문 이벤트 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }

    private void handleEvent(Event<EventData> event) {
        switch (event.getType()) {
            case ORDER_CREATE:
                OrderCreateData data = (OrderCreateData) event.getData();
                log.info("주문 생성 이벤트 수신: feedId={}, quantity={}", data.feedId(), data.quantity());
                decreaseStockUseCase.decreaseStock(data.feedId(), data.quantity());
                break;
            case ORDER_CANCEL:
                OrderCancelData cancelData = (OrderCancelData) event.getData();
                log.info("주문 취소 이벤트 수신: feedId={}, quantity={}", cancelData.feedId(),
                    cancelData.quantity());
                restoreStockUseCase.restoreStock(cancelData.feedId(), cancelData.quantity());
                break;
            default:
                log.warn("처리하지 않는 이벤트 타입입니다: {}", event.getType());
        }
    }
} 
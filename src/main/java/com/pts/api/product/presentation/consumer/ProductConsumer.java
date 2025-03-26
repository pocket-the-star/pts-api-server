package com.pts.api.product.presentation.consumer;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType.Topic;
import com.pts.api.lib.internal.shared.event.data.FeedCreateData;
import com.pts.api.product.application.port.in.PriceUpdateUseCase;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductConsumer {

    private final PriceUpdateUseCase priceUpdateUseCase;

    @KafkaListener(topics = {
        Topic.FEED_CREATE
    }, groupId = "product-group")
    public void feedCreate(String message) {
        try {
            Event<EventData> event = Event.fromJson(message);
            handleEvent(Objects.requireNonNull(event));

            log.info("상품 메세지 처리 완료: {}", message);
        } catch (Exception e) {

            log.error("상품 메시지 처리 중 오류 발생. 메시지: {}", message, e);
        }
    }

    private void handleEvent(Event<EventData> event) {
        switch (event.getType()) {
            case FEED_CREATE:
                FeedCreateData feedCreateData = (FeedCreateData) event.getData();
                priceUpdateUseCase.updatePrice(feedCreateData.productId(), feedCreateData.price());
                break;
            default:
                throw new NotFoundException("존재하지 않는 이벤트 타입입니다.: " + event.getType());
        }
    }


}

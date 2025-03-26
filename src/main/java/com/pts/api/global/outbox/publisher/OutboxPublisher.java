package com.pts.api.global.outbox.publisher;

import com.pts.api.global.outbox.model.Outbox;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxPublisher implements EventPublisherPort {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final DateTimeUtil dateTimeUtil;

    public void publish(EventType type, EventData payload) {
        LocalDateTime now = dateTimeUtil.now();
        Outbox outbox = Outbox
            .builder()
            .eventType(type)
            .data(Event
                .builder()
                .type(type)
                .payload(payload)
                .build()
                .toJson())
            .createdAt(now)
            .updatedAt(now)
            .build();

        applicationEventPublisher.publishEvent(outbox);
    }
}

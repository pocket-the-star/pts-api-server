package com.pts.api.global.outbox.publisher;

import com.pts.api.global.outbox.model.Outbox;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType type, EventData payload) {
        Outbox outbox = Outbox
            .builder()
            .eventType(type)
            .data(Event
                .builder()
                .type(type)
                .payload(payload)
                .build()
                .toJson())
            .build();

        applicationEventPublisher.publishEvent(outbox);
    }
}

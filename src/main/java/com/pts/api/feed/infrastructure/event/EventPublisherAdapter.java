package com.pts.api.feed.infrastructure.event;

import com.pts.api.feed.application.port.out.EventPublisherPort;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.EventData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisherPort {

    private final OutboxPublisher outboxPublisher;

    @Override
    public void publish(EventType type, EventData data) {
        outboxPublisher.publish(type, data);
    }
} 
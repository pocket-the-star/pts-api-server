package com.pts.api.global.outbox.publisher;

import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType;

public interface EventPublisherPort {

    void publish(EventType eventType, EventData eventData);
} 
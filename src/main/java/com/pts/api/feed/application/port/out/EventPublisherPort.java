package com.pts.api.feed.application.port.out;

import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventType;

public interface EventPublisherPort {
    void publish(EventType type, EventData data);
} 
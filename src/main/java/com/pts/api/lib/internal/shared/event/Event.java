package com.pts.api.lib.internal.shared.event;

import com.pts.api.lib.internal.shared.util.serializer.DataSerializer;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Event<T extends EventData> {

    private EventType type;
    private T data;

    @Builder
    private Event(EventType type, T payload) {
        this.type = type;
        this.data = payload;
    }

    public static Event<EventData> fromJson(String json) {
        EventRaw eventRaw = DataSerializer.deserialize(json, EventRaw.class);
        if (eventRaw == null) {
            return null;
        }
        return Event.<EventData>builder()
            .type(EventType.from(eventRaw.getType()))
            .payload(DataSerializer.deserialize(eventRaw.getData(),
                Objects.requireNonNull(EventType.from(eventRaw.getType())).getDataClass()))
            .build();
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    @Getter
    private static class EventRaw {

        private String type;
        private Object data;
    }
}

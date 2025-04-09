package com.pts.api.lib.internal.shared.event;

public interface EventHandler<T extends EventData> {

    void handle(Event<T> event);

    boolean supports(Event<T> event);
}

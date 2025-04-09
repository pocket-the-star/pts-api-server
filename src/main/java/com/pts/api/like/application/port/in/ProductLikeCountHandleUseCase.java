package com.pts.api.like.application.port.in;

import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventData;

public interface ProductLikeCountHandleUseCase {

    void handleEvent(Event<EventData> event);

}

package com.pts.api.like.application.service.handler;

import com.pts.api.lib.internal.shared.event.EventData;
import com.pts.api.lib.internal.shared.event.EventHandler;

public interface ProductLikeCountEventHandler<T extends EventData> extends EventHandler<T> {

}

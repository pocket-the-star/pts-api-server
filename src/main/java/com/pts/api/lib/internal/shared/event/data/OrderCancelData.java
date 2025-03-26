package com.pts.api.lib.internal.shared.event.data;

import com.pts.api.lib.internal.shared.event.EventData;

public record OrderCancelData(Long feedId, Integer quantity) implements EventData {
} 
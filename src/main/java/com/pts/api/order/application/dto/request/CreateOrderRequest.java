package com.pts.api.order.application.dto.request;

import com.pts.api.order.domain.model.Order;
import lombok.NonNull;

public record CreateOrderRequest(
    @NonNull
    Long feedId,
    @NonNull
    Integer quantity,
    @NonNull
    CreateShippingRequest shipping
) {

    public Order toOrder() {
        return Order.builder()
            .feedId(feedId)
            .quantity(quantity)
            .build();
    }
} 
package com.pts.api.order.dto.request;

import com.pts.api.order.model.Order;
import lombok.NonNull;

public record CreateOrderRequestDto(
    @NonNull
    Long feedId,
    @NonNull
    Integer quantity,
    @NonNull
    CreateShippingRequestDto shipping
) {

    public Order toOrder() {
        return Order.builder()
            .feedId(feedId)
            .quantity(quantity)
            .build();
    }
}

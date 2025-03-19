package com.pts.api.order.dto.request;

import com.pts.api.lib.internal.shared.enums.OrderStatus;

public record UpdateOrderRequestDto(OrderStatus orderStatus) {

}

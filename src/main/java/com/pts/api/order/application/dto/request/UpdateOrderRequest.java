package com.pts.api.order.application.dto.request;

import com.pts.api.lib.internal.shared.enums.OrderStatus;

public record UpdateOrderRequest(OrderStatus orderStatus) {

}
package com.pts.api.order.application.port.in;

import com.pts.api.order.application.dto.request.CreateOrderRequest;

public interface CreateOrderUseCase {

    void create(Long userId, CreateOrderRequest request);
} 
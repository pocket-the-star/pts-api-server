package com.pts.api.order.application.port.in;

import com.pts.api.order.application.dto.request.UpdateOrderRequest;

public interface UpdateOrderUseCase {

    void updateStatus(Long userId, Long orderId, UpdateOrderRequest request);

    void cancel(Long userId, Long orderId);

    void delete(Long userId, Long orderId);
} 
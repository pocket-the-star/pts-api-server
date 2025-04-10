package com.pts.api.order.application.port.in;

public interface CancelOrderUseCase {

    void cancel(Long userId, Long orderId);
}

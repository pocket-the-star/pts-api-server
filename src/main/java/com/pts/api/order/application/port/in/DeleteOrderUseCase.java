package com.pts.api.order.application.port.in;

public interface DeleteOrderUseCase {

    void delete(Long userId, Long orderId);
}

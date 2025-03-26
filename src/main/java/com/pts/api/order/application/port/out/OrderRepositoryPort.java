package com.pts.api.order.application.port.out;

import com.pts.api.order.domain.model.Order;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    void delete(Order order);
} 
package com.pts.api.order.infrastructure.persistence.adapter;

import com.pts.api.order.application.port.out.OrderRepositoryPort;
import com.pts.api.order.domain.model.Order;
import com.pts.api.order.infrastructure.persistence.entity.OrderEntity;
import com.pts.api.order.infrastructure.persistence.repository.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(OrderEntity.fromModel(order)).toModel();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id)
            .map(OrderEntity::toModel);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(OrderEntity.fromModel(order));
    }
} 
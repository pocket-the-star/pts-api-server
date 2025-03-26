package com.pts.api.order.infrastructure.persistence.repository;

import com.pts.api.order.infrastructure.persistence.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
} 
package com.pts.api.order.infrastructure.persistence.repository;

import com.pts.api.order.infrastructure.persistence.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {

}
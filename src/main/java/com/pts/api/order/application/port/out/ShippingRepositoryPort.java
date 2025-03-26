package com.pts.api.order.application.port.out;

import com.pts.api.order.domain.model.Shipping;
import java.util.Optional;

public interface ShippingRepositoryPort {
    Shipping save(Shipping shipping);
    Optional<Shipping> findById(Long id);
    void delete(Shipping shipping);
} 
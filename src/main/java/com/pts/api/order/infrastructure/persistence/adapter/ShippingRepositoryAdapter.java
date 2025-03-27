package com.pts.api.order.infrastructure.persistence.adapter;

import com.pts.api.order.application.port.out.ShippingRepositoryPort;
import com.pts.api.order.domain.model.Shipping;
import com.pts.api.order.infrastructure.persistence.entity.ShippingEntity;
import com.pts.api.order.infrastructure.persistence.repository.ShippingRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShippingRepositoryAdapter implements ShippingRepositoryPort {

    private final ShippingRepository shippingRepository;

    @Override
    public Shipping save(Shipping shipping) {
        ShippingEntity entity = ShippingEntity.fromModel(shipping);
        ShippingEntity savedEntity = shippingRepository.save(entity);
        return savedEntity.toModel();
    }

    @Override
    public Optional<Shipping> findById(Long id) {
        return shippingRepository.findById(id)
            .map(ShippingEntity::toModel);
    }

    @Override
    public void delete(Shipping shipping) {
        ShippingEntity entity = ShippingEntity.fromModel(shipping);
        shippingRepository.delete(entity);
    }
} 
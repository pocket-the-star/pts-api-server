package com.pts.api.like.infrastructure.persistence.adapter;

import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import com.pts.api.like.infrastructure.persistence.entity.ProductLikeEntity;
import com.pts.api.like.infrastructure.persistence.repository.ProductLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLikeRepositoryAdapter implements ProductLikeRepositoryPort {

    private final ProductLikeRepository productLikeRepository;

    @Override
    public ProductLike save(ProductLike productLike) {

        return productLikeRepository.save(ProductLikeEntity.fromDomain(productLike)).toDomain();
    }

    @Override
    public Optional<ProductLike> findByProductIdAndUserId(Long productId, Long userId) {
        return productLikeRepository.findOneByProductIdAndUserId(productId, userId)
            .map(ProductLikeEntity::toDomain);
    }

    @Override
    public boolean existsByProductIdAndUserId(Long productId, Long userId) {
        return findByProductIdAndUserId(productId, userId).isPresent();
    }

    @Override
    public void delete(ProductLike productLike) {
        productLikeRepository.deleteById(productLike.getId());
    }
} 
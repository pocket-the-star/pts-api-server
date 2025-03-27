package com.pts.api.like.infrastructure.persistence.adapter;

import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import com.pts.api.like.infrastructure.persistence.entity.ProductLikeCountEntity;
import com.pts.api.like.infrastructure.persistence.repository.ProductLikeCountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductLikeCountRepositoryAdapter implements ProductLikeCountRepositoryPort {

    private final ProductLikeCountRepository productLikeCountRepository;

    @Override
    public ProductLikeCount save(ProductLikeCount productLikeCount) {

        return productLikeCountRepository.save(ProductLikeCountEntity.fromDomain(productLikeCount))
            .toDomain();
    }

    @Override
    public Optional<ProductLikeCount> findByProductId(Long productId) {
        return productLikeCountRepository.findOneByProductId(productId)
            .map(ProductLikeCountEntity::toDomain);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return findByProductId(productId).isPresent();
    }
}
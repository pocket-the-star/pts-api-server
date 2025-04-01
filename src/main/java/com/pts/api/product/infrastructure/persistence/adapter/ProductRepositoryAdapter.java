package com.pts.api.product.infrastructure.persistence.adapter;

import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import com.pts.api.product.infrastructure.persistence.entity.ProductEntity;
import com.pts.api.product.infrastructure.persistence.repository.ProductRepository;
import com.pts.api.product.infrastructure.persistence.repository.QProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final QProductRepository qProductRepository;

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.fromModel(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return savedEntity.toModel();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
            .map(ProductEntity::toModel);
    }

    @Override
    public void delete(Product product) {
        ProductEntity entity = ProductEntity.fromModel(product);
        productRepository.delete(entity);
    }

    @Override
    public List<Product> findAll(Long idolId, Long categoryId, Long subCategoryId, Long offset,
        int limit) {
        return qProductRepository.findAll(idolId, categoryId, subCategoryId, offset, limit);
    }
} 
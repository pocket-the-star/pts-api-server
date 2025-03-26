package com.pts.api.product.infrastructure.persistence.adapter;

import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import com.pts.api.product.infrastructure.persistence.model.ProductEntity;
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
        ProductEntity entity = ProductEntity.from(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id)
            .map(ProductEntity::toDomain);
    }

    @Override
    public void delete(Product product) {
        ProductEntity entity = ProductEntity.from(product);
        productRepository.delete(entity);
    }

    @Override
    public List<Product> findAll(Long artistId, Long categoryId, Long subCategoryId, int offset) {
        return qProductRepository.findAll(artistId, categoryId, subCategoryId, offset);
    }
} 
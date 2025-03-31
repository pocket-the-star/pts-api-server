package com.pts.api.product.application.port.out;

import com.pts.api.product.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    void delete(Product product);

    List<Product> findAll(Long artistId, Long categoryId, Long subCategoryId, Long offset,
        int limit);
} 
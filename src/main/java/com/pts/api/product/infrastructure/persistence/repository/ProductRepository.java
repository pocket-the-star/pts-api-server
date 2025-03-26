package com.pts.api.product.infrastructure.persistence.repository;

import com.pts.api.product.infrastructure.persistence.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}

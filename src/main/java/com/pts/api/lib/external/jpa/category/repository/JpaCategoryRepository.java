package com.pts.api.lib.external.jpa.category.repository;


import com.pts.api.lib.external.jpa.category.model.CategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAll();

    Optional<CategoryEntity> findOneById(Long id);
}

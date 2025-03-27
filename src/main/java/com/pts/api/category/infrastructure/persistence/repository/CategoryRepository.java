package com.pts.api.category.infrastructure.persistence.repository;

import com.pts.api.category.infrastructure.persistence.entity.CategoryEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findOneById(Long id);
}

package com.pts.api.category.infrastructure.persistence.adapter;

import com.pts.api.category.application.port.out.CategoryRepositoryPort;
import com.pts.api.category.domain.model.Category;
import com.pts.api.category.infrastructure.persistence.entity.CategoryEntity;
import com.pts.api.category.infrastructure.persistence.repository.CategoryRepository;
import com.pts.api.global.infrastructure.cache.CustomCacheable;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findOneById(Long id) {
        return categoryRepository.findOneById(id).map(CategoryEntity::toModel);
    }

    @Override
    @CustomCacheable(
        prefix = "categories",
        ttlSeconds = 60 * 30
    )
    public List<Category> findAll() {
        return categoryRepository.findAll().stream().map(CategoryEntity::toModel).toList();
    }
}
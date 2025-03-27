package com.pts.api.category.application.port.out;

import com.pts.api.category.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {

    Optional<Category> findOneById(Long id);

    List<Category> findAll();
}
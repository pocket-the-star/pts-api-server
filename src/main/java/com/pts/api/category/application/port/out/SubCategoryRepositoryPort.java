package com.pts.api.category.application.port.out;

import com.pts.api.category.domain.model.SubCategory;
import java.util.List;
import java.util.Optional;

public interface SubCategoryRepositoryPort {

    List<SubCategory> findAllByCategoryId(Long categoryId);

    Optional<SubCategory> findOneById(Long categoryId, Long id);
}

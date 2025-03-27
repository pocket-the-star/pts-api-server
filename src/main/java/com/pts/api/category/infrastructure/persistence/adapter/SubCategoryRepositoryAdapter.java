package com.pts.api.category.infrastructure.persistence.adapter;

import com.pts.api.category.application.port.out.SubCategoryRepositoryPort;
import com.pts.api.category.domain.model.SubCategory;
import com.pts.api.category.infrastructure.persistence.entity.SubCategoryEntity;
import com.pts.api.category.infrastructure.persistence.repository.SubCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubCategoryRepositoryAdapter implements SubCategoryRepositoryPort {

    private final SubCategoryRepository subCategoryRepository;

    @Override
    public List<SubCategory> findAllByCategoryId(Long categoryId) {
        return subCategoryRepository.findAllByCategoryId(categoryId).stream()
            .map(SubCategoryEntity::toModel).toList();
    }

    @Override
    public Optional<SubCategory> findOneById(Long categoryId, Long id) {
        return subCategoryRepository.findOneByIdAndCategoryId(categoryId, id)
            .map(SubCategoryEntity::toModel);
    }
}

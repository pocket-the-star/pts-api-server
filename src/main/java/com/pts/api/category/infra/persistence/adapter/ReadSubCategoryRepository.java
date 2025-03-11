package com.pts.api.category.infra.persistence.adapter;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.application.port.out.ReadSubCategoryRepositoryPort;
import com.pts.api.category.infra.mapper.IReadSubCategoryMapper;
import com.pts.api.lib.external.jpa.category.repository.JpaSubCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ReadSubCategoryRepository implements ReadSubCategoryRepositoryPort {

    private final IReadSubCategoryMapper readSubCategoryMapper;
    private final JpaSubCategoryRepository subCategoryRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<GetSubCategoryResponseDto> findOneById(Long categoryId, Long id) {
        return subCategoryRepository.findOneById(categoryId, id)
            .map(readSubCategoryMapper::mapToSubCategory);
    }

    @Override
    public List<GetSubCategoryResponseDto> findAllByCategoryId(Long categoryId) {
        return readSubCategoryMapper.mapToSubCategories(
            subCategoryRepository.findAllByCategoryId(categoryId));
    }
}

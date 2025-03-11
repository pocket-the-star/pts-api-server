package com.pts.api.category.infra.persistence.adapter;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.category.application.port.out.ReadCategoryRepositoryPort;
import com.pts.api.category.infra.mapper.IReadCategoryMapper;
import com.pts.api.lib.external.jpa.category.repository.JpaCategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadCategoryRepository implements ReadCategoryRepositoryPort {

    private final JpaCategoryRepository categoryJpaRepository;
    private final IReadCategoryMapper categoryMapper;

    @Override
    public List<GetCategoryResponseDto> findAll() {
        return categoryMapper.mapToCategories(categoryJpaRepository.findAll());
    }

    @Override
    public Optional<GetCategoryResponseDto> findOneById(Long id) {
        return categoryJpaRepository.findOneById(id).map(categoryMapper::mapToCategory);
    }

}

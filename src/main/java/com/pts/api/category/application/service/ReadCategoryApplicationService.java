package com.pts.api.category.application.service;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import com.pts.api.category.application.port.in.ReadCategoryUseCase;
import com.pts.api.category.application.port.out.CategoryRepositoryPort;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadCategoryApplicationService implements ReadCategoryUseCase {

    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ReadCategoryResponse getCategory(Long id) {
        return ReadCategoryResponse.fromModel(categoryRepositoryPort.findOneById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadCategoryResponse> getCategories() {
        return categoryRepositoryPort.findAll().stream().map(ReadCategoryResponse::fromModel)
            .toList();
    }


}

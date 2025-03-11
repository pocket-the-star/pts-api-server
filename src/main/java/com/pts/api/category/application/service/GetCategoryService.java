package com.pts.api.category.application.service;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.category.application.port.in.GetCategoryUseCase;
import com.pts.api.category.application.port.out.ReadCategoryRepositoryPort;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetCategoryService implements GetCategoryUseCase {

    private final ReadCategoryRepositoryPort readCategoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetCategoryResponseDto getCategory(Long id) {
        return readCategoryRepositoryPort.findOneById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetCategoryResponseDto> getCategories() {
        return readCategoryRepositoryPort.findAll();
    }
}

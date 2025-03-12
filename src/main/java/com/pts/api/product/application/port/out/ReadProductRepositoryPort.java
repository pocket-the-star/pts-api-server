package com.pts.api.product.application.port.out;

import java.util.List;

import com.pts.api.product.application.dto.response.GetProductResponseDto;

public interface ReadProductRepositoryPort {
    public List<GetProductResponseDto> findAll(Long groupId, Long categoryId, Long subCategoryId, int offset);
}

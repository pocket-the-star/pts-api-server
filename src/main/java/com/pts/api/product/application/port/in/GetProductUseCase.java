package com.pts.api.product.application.port.in;

import com.pts.api.product.application.dto.response.GetProductResponseDto;
import java.util.List;

public interface GetProductUseCase {

    public List<GetProductResponseDto> getProducts(Long groupId, Long categoryId,
        Long subCategoryId, int offset);
}

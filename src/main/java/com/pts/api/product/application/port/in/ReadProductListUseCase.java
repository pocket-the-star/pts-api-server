package com.pts.api.product.application.port.in;

import com.pts.api.product.application.dto.response.ProductResponse;
import java.util.List;

public interface ReadProductListUseCase {

    List<ProductResponse> findAll(Long idolId, Long categoryId, Long subCategoryId, Long offset,
        int limit);
} 
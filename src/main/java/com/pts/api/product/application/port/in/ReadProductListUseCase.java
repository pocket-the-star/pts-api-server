package com.pts.api.product.application.port.in;

import com.pts.api.product.application.dto.response.ProductResponse;
import java.util.List;

public interface ReadProductListUseCase {

    List<ProductResponse> findAll(Long artistId, Long categoryId, Long subCategoryId, int offset);
} 
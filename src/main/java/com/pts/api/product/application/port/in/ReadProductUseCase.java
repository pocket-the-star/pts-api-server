package com.pts.api.product.application.port.in;

import com.pts.api.product.application.dto.response.ProductResponse;

public interface ReadProductUseCase {

    ProductResponse findById(Long id);
} 
package com.pts.api.product.application.port.in;

import com.pts.api.product.application.dto.request.CreateProductRequest;

public interface PostProductUseCase {

    void create(CreateProductRequest request);
} 
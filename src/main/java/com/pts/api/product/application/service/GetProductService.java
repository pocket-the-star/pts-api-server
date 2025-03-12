package com.pts.api.product.application.service;

import com.pts.api.product.application.dto.response.GetProductResponseDto;
import com.pts.api.product.application.port.in.GetProductUseCase;
import com.pts.api.product.application.port.out.ReadProductRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {

    private final ReadProductRepositoryPort readProductRepositoryPort;

    @Override
    public List<GetProductResponseDto> getProducts(Long groupId, Long categoryId,
        Long subCategoryId, int offset) {
        return readProductRepositoryPort.findAll(groupId, categoryId, subCategoryId, offset);
    }
}

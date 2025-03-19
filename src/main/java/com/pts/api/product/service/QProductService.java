package com.pts.api.product.service;

import com.pts.api.product.dto.response.GetProductResponseDto;
import com.pts.api.product.exception.ProductNotFoundException;
import com.pts.api.product.model.Product;
import com.pts.api.product.repository.QProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QProductService {

    private final QProductRepository QProductRepository;

    public List<GetProductResponseDto> getProducts(Long artistId, Long categoryId,
        Long subCategoryId, int offset) {
        return QProductRepository.findAll(artistId, categoryId, subCategoryId, offset).stream()
            .map(this::toDto)
            .toList();
    }

    public GetProductResponseDto getProduct(Long id) {
        return toDto(QProductRepository.findOneById(id)
            .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품입니다. id: " + id)));
    }

    private GetProductResponseDto toDto(Product product) {
        return new GetProductResponseDto(
            product.getId(),
            product.getTitle(),
            product.getProductThumbnail(),
            product.getMinBuyPrice(),
            product.getMaxSellPrice(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}

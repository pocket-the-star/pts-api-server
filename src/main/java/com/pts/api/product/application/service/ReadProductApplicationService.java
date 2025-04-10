package com.pts.api.product.application.service;

import com.pts.api.product.application.dto.response.ProductResponse;
import com.pts.api.product.application.port.in.ReadProductUseCase;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.common.exception.ProductNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadProductApplicationService implements ReadProductUseCase {

    private final ProductRepositoryPort productRepository;

    /**
     * 상품 단건 조회
     *
     * @param id 상품 ID
     * @return 상품 정보
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
            .map(ProductResponse::from)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    /**
     * 상품 목록 조회
     *
     * @param idolId        아이돌 ID
     * @param categoryId    카테고리 ID
     * @param subCategoryId 서브 카테고리 ID
     * @param offset        오프셋
     * @param limit         한 페이지에 보여줄 상품 개수
     * @return 상품 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Long idolId, Long categoryId, Long subCategoryId,
        Long offset, int limit) {
        return productRepository.findAll(idolId, categoryId, subCategoryId, offset, limit)
            .stream()
            .map(ProductResponse::from)
            .toList();
    }
}
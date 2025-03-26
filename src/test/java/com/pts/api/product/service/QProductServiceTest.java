package com.pts.api.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.product.dto.response.GetProductResponseDto;
import com.pts.api.product.infrastructure.persistence.repository.QProductRepository;
import com.pts.api.product.model.Product;
import com.pts.api.product.model.ProductImage;
import com.pts.api.product.model.ProductOption;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetProductService 클래스")
class QProductServiceTest extends BaseUnitTest {

    @Mock
    private QProductRepository QProductRepository;

    private QProductService QProductService;

    private final Long GROUP_ID = 1L;
    private final Long CATEGORY_ID = 2L;
    private final Long SUBCATEGORY_ID = 3L;
    private final int OFFSET = 0;
    private final LocalDateTime NOW = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Given
        QProductService = new QProductService(QProductRepository);
    }

    @Nested
    @DisplayName("getProducts 메서드 호출 시")
    class DescribeGetProducts {

        @Test
        @DisplayName("유효한 파라미터로 조회 시 상품 리스트를 반환한다")
        void itReturnsProductList() {
            // Given

            List<GetProductResponseDto> expectedList = List.of(
                new GetProductResponseDto(
                    101L, "Product1", "http://image1.jpg",
                    100, 200, NOW, NOW
                ),
                new GetProductResponseDto(
                    102L, "Product2", "http://image2.jpg",
                    150, 250, NOW, NOW
                )
            );
            List<Product> products = List.of(
                Product.builder()
                    .id(101L)
                    .title("Product1")
                    .options(List.of(
                        ProductOption.builder().build()
                    ))
                    .images(List.of(
                        ProductImage.builder().imageUrl("http://image1.jpg").build()
                    ))
                    .minBuyPrice(100)
                    .maxSellPrice(200)
                    .createdAt(NOW)
                    .updatedAt(NOW)
                    .build(),
                Product.builder()
                    .id(102L)
                    .title("Product2")
                    .options(List.of(
                        ProductOption.builder().build()
                    ))
                    .images(List.of(
                        ProductImage.builder().imageUrl("http://image2.jpg").build()
                    ))
                    .minBuyPrice(150)
                    .maxSellPrice(250)
                    .createdAt(NOW)
                    .updatedAt(NOW)
                    .build()
            );
            when(QProductRepository.findAll(GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID, OFFSET))
                .thenReturn(products);

            // When
            List<GetProductResponseDto> actualList = QProductService.getProducts(GROUP_ID,
                CATEGORY_ID, SUBCATEGORY_ID, OFFSET);

            // Then
            assertEquals(expectedList, actualList);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 리스트를 반환한다")
        void itReturnsEmptyList() {
            // Given
            when(QProductRepository.findAll(GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID, OFFSET))
                .thenReturn(List.of());
            // When
            List<GetProductResponseDto> actualList = QProductService.getProducts(GROUP_ID,
                CATEGORY_ID, SUBCATEGORY_ID, OFFSET);
            // Then
            assertEquals(0, actualList.size());
        }
    }
}

package com.pts.api.unit.product.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.product.application.dto.response.GetProductResponseDto;
import com.pts.api.product.application.port.out.ReadProductRepositoryPort;
import com.pts.api.product.application.service.GetProductService;
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
class GetProductServiceTest extends BaseUnitTest {

    @Mock
    private ReadProductRepositoryPort readProductRepositoryPort;

    private GetProductService getProductService;

    private final Long GROUP_ID = 1L;
    private final Long CATEGORY_ID = 2L;
    private final Long SUBCATEGORY_ID = 3L;
    private final int OFFSET = 0;

    @BeforeEach
    void setUp() {
        // Given
        getProductService = new GetProductService(readProductRepositoryPort);
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
                    101L, "Product1", "http://image1.jpg", GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID,
                    100, 200, "2023-01-01T00:00:00", "2023-01-02T00:00:00"
                ),
                new GetProductResponseDto(
                    102L, "Product2", "http://image2.jpg", GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID,
                    150, 250, "2023-01-03T00:00:00", "2023-01-04T00:00:00"
                )
            );
            when(readProductRepositoryPort.findAll(GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID, OFFSET))
                .thenReturn(expectedList);
            // When
            List<GetProductResponseDto> actualList = getProductService.getProducts(GROUP_ID,
                CATEGORY_ID, SUBCATEGORY_ID, OFFSET);
            // Then
            assertEquals(expectedList, actualList);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 리스트를 반환한다")
        void itReturnsEmptyList() {
            // Given
            when(readProductRepositoryPort.findAll(GROUP_ID, CATEGORY_ID, SUBCATEGORY_ID, OFFSET))
                .thenReturn(List.of());
            // When
            List<GetProductResponseDto> actualList = getProductService.getProducts(GROUP_ID,
                CATEGORY_ID, SUBCATEGORY_ID, OFFSET);
            // Then
            assertEquals(0, actualList.size());
        }
    }
}

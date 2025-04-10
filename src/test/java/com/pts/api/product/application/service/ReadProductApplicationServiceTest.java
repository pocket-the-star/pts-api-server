package com.pts.api.product.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.product.application.dto.response.ProductResponse;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.common.exception.ProductNotFoundException;
import com.pts.api.product.domain.model.Product;
import com.pts.api.product.domain.model.ProductImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("ReadProductApplicationService 클래스")
class ReadProductApplicationServiceTest extends BaseUnitTest {

    @Mock
    private ProductRepositoryPort productRepository;

    private ReadProductApplicationService readProductApplicationService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_TITLE = "테스트 상품";
    private static final Long TEST_SUB_CATEGORY_ID = 1L;
    private static final Long TEST_ARTIST_ID = 1L;
    private static final Integer TEST_MIN_BUY_PRICE = 10000;
    private static final Integer TEST_MAX_SELL_PRICE = 20000;
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();
    private static final String TEST_IMAGE_URL = "image1.jpg";

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder().id(TEST_ID).title(TEST_TITLE)
            .subCategoryId(TEST_SUB_CATEGORY_ID).artistId(TEST_ARTIST_ID)
            .maxSellPrice(TEST_MAX_SELL_PRICE).minBuyPrice(TEST_MIN_BUY_PRICE).images(List.of(
                ProductImage.builder().id(TEST_ID).imageUrl(TEST_IMAGE_URL).sortOrder(0)
                    .createdAt(TEST_DATE).updatedAt(TEST_DATE).build())).createdAt(TEST_DATE)
            .updatedAt(TEST_DATE).build();

        readProductApplicationService = new ReadProductApplicationService(productRepository);
    }

    @Nested
    @DisplayName("findById 메서드 호출 시")
    class DescribeFindById {

        @Test
        @DisplayName("존재하는 상품이면 상품 정보를 반환한다")
        void returnsProductResponse() {
            // Given
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(testProduct));

            // When
            ProductResponse response = readProductApplicationService.findById(TEST_ID);

            // Then
            assertThat(response.id()).isEqualTo(TEST_ID);
            assertThat(response.title()).isEqualTo(TEST_TITLE);
            assertThat(response.subCategoryId()).isEqualTo(TEST_SUB_CATEGORY_ID);
            assertThat(response.artistId()).isEqualTo(TEST_ARTIST_ID);
            assertThat(response.minBuyPrice()).isEqualTo(TEST_MIN_BUY_PRICE);
            assertThat(response.maxSellPrice()).isEqualTo(TEST_MAX_SELL_PRICE);
        }

        @Test
        @DisplayName("존재하지 않는 상품이면 ProductNotFoundException을 발생시킨다")
        void throwsProductNotFoundException() {
            // Given
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> readProductApplicationService.findById(TEST_ID)).isInstanceOf(
                ProductNotFoundException.class).hasMessage("Product not found with id: " + TEST_ID);
        }
    }

    @Nested
    @DisplayName("findAll 메서드 호출 시")
    class DescribeFindAll {

        @Test
        @DisplayName("상품 목록을 반환한다")
        void returnsProductList() {
            // Given
            List<Product> products = List.of(testProduct);
            when(
                productRepository
                    .findAll(null, null, null, 0L, 20))
                .thenReturn(products);

            // When
            List<ProductResponse> responses = readProductApplicationService.findAll(null, null,
                null, 0L, 20);

            // Then
            assertThat(responses).hasSize(1);
            ProductResponse response = responses.get(0);
            assertThat(response.id()).isEqualTo(TEST_ID);
            assertThat(response.title()).isEqualTo(TEST_TITLE);
            assertThat(response.subCategoryId()).isEqualTo(TEST_SUB_CATEGORY_ID);
            assertThat(response.artistId()).isEqualTo(TEST_ARTIST_ID);
            assertThat(response.minBuyPrice()).isEqualTo(TEST_MIN_BUY_PRICE);
            assertThat(response.maxSellPrice()).isEqualTo(TEST_MAX_SELL_PRICE);
        }
    }
}
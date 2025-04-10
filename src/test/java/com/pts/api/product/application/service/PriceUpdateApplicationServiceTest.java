package com.pts.api.product.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
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
import org.redisson.api.RLock;

@DisplayName("PriceUpdateApplicationService 클래스")
class PriceUpdateApplicationServiceTest extends BaseUnitTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private LockRepository productLockRepository;

    @Mock
    private DateTimeUtil dateTimeUtil;

    private PriceUpdateApplicationService priceUpdateApplicationService;

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
        testProduct = Product.builder()
            .id(TEST_ID)
            .title(TEST_TITLE)
            .subCategoryId(TEST_SUB_CATEGORY_ID)
            .artistId(TEST_ARTIST_ID)
            .maxSellPrice(TEST_MAX_SELL_PRICE)
            .minBuyPrice(TEST_MIN_BUY_PRICE)
            .images(List.of(ProductImage.builder()
                .id(TEST_ID)
                .imageUrl(TEST_IMAGE_URL)
                .sortOrder(0)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build()))
            .createdAt(TEST_DATE)
            .updatedAt(TEST_DATE)
            .build();

        priceUpdateApplicationService = new PriceUpdateApplicationService(
            productRepository,
            productLockRepository,
            dateTimeUtil
        );
    }

    @Nested
    @DisplayName("updatePrice 메서드 호출 시")
    class DescribeUpdatePrice {

        @Test
        @DisplayName("존재하는 상품의 가격을 업데이트한다")
        void updatesPriceOfExistingProduct() {
            // Given
            int newPrice = 15000;
            RLock mockLock = mock(RLock.class);
            when(productLockRepository.getFairLock(any())).thenReturn(mockLock);
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(testProduct));
            when(dateTimeUtil.now()).thenReturn(TEST_DATE);

            // When
            priceUpdateApplicationService.updatePrice(TEST_ID, newPrice);

            // Then
            verify(productRepository).save(any(Product.class));
            assertThat(testProduct.getMaxSellPrice()).isEqualTo(
                Math.max(TEST_MAX_SELL_PRICE, newPrice));
            assertThat(testProduct.getMinBuyPrice()).isEqualTo(
                Math.min(TEST_MIN_BUY_PRICE, newPrice));
        }

        @Test
        @DisplayName("존재하지 않는 상품이면 아무 작업도 하지 않는다")
        void doesNothingForNonExistingProduct() {
            // Given
            RLock mockLock = mock(RLock.class);
            when(productLockRepository.getFairLock(any())).thenReturn(mockLock);
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

            // When
            priceUpdateApplicationService.updatePrice(TEST_ID, 15000);

            // Then
            verify(productRepository, never()).save(any(Product.class));
        }
    }
}
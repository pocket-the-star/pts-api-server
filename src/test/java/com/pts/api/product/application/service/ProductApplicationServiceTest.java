package com.pts.api.product.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.dto.request.CreateProductRequest;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductApplicationService 클래스")
class ProductApplicationServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private LockRepository productLockRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @Mock
    private DateTimeUtil dateTimeUtil;

    @InjectMocks
    private ProductApplicationService productApplicationService;

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
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Test
        @DisplayName("유효한 요청이면 상품을 생성하고 이벤트를 발행한다")
        void createsProductAndPublishesEvent() {
            // Given
            CreateProductRequest request = new CreateProductRequest(
                TEST_ID,
                TEST_TITLE,
                TEST_SUB_CATEGORY_ID,
                TEST_ARTIST_ID,
                List.of(TEST_IMAGE_URL)
            );

            when(dateTimeUtil.now()).thenReturn(TEST_DATE);
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);
            doNothing().when(eventPublisher).publish(any(EventType.class), any(ProductCreateData.class));

            // When
            productApplicationService.create(request);

            // Then
            verify(productRepository).save(any(Product.class));
            verify(eventPublisher).publish(EventType.PRODUCT_CREATE, new ProductCreateData(TEST_ID));
        }
    }

    @Nested
    @DisplayName("updatePrice 메서드 호출 시")
    class DescribeUpdatePrice {

        @Test
        @DisplayName("존재하는 상품의 가격을 업데이트한다")
        void updatesPriceOfExistingProduct() {
            // Given
            Integer newPrice = 15000;
            RLock mockLock = mock(RLock.class);
            when(productLockRepository.getFairLock(any())).thenReturn(mockLock);
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.of(testProduct));
            when(dateTimeUtil.now()).thenReturn(TEST_DATE);

            // When
            productApplicationService.updatePrice(TEST_ID, newPrice);

            // Then
            verify(productRepository).save(any(Product.class));
            assertThat(testProduct.getMaxSellPrice()).isEqualTo(Math.max(TEST_MAX_SELL_PRICE, newPrice));
            assertThat(testProduct.getMinBuyPrice()).isEqualTo(Math.min(TEST_MIN_BUY_PRICE, newPrice));
        }

        @Test
        @DisplayName("존재하지 않는 상품이면 아무 작업도 하지 않는다")
        void doesNothingForNonExistingProduct() {
            // Given
            RLock mockLock = mock(RLock.class);
            when(productLockRepository.getFairLock(any())).thenReturn(mockLock);
            when(productRepository.findById(TEST_ID)).thenReturn(Optional.empty());

            // When
            productApplicationService.updatePrice(TEST_ID, 15000);

            // Then
            verify(productRepository, never()).save(any(Product.class));
        }
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
            ProductResponse response = productApplicationService.findById(TEST_ID);

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
            assertThatThrownBy(() -> productApplicationService.findById(TEST_ID))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found with id: " + TEST_ID);
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
            when(productRepository.findAll(null, null, null, 0L, 20)).thenReturn(products);

            // When
            List<ProductResponse> responses = productApplicationService.findAll(null, null, null, 0L, 20);

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
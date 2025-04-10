package com.pts.api.product.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import com.pts.api.product.domain.model.ProductImage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("PostProductApplicationService 클래스")
class PostProductApplicationServiceTest extends BaseUnitTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private EventPublisherPort eventPublisher;

    @Mock
    private DateTimeUtil dateTimeUtil;

    private PostProductApplicationService postProductApplicationService;

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

        postProductApplicationService = new PostProductApplicationService(
            productRepository,
            eventPublisher,
            dateTimeUtil
        );
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
            doNothing().when(eventPublisher)
                .publish(any(EventType.class), any(ProductCreateData.class));

            // When
            postProductApplicationService.create(request);

            // Then
            verify(productRepository).save(any(Product.class));
            verify(eventPublisher).publish(EventType.PRODUCT_CREATE,
                new ProductCreateData(TEST_ID));
        }
    }
} 
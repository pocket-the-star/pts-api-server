package com.pts.api.feed.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import com.pts.api.user.common.exception.AlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("PostFeedApplicationService 클래스")
class PostFeedApplicationServiceTest extends BaseUnitTest {

    @Mock
    private FeedRepositoryPort feedRepository;
    @Mock
    private ProductRepositoryPort productRepository;
    @Mock
    private EventPublisherPort eventPublisher;
    @Mock
    private DateTimeUtil dateTimeUtil;

    private PostFeedApplicationService feedService;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final String TEST_CONTENT = "테스트 피드 내용";
    private static final List<String> TEST_IMAGE_URLS = List.of("test1.jpg", "test2.jpg");
    private static final ProductGrade TEST_GRADE = ProductGrade.A;
    private static final Integer TEST_PRICE = 10000;
    private static final Integer TEST_QUANTITY = 10;
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        feedService = new PostFeedApplicationService(feedRepository, productRepository,
            eventPublisher,
            dateTimeUtil);
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Nested
        @DisplayName("존재하는 상품 ID가 주어지면")
        class WithExistingProductId {

            @Test
            @DisplayName("피드를 생성한다")
            void itCreatesFeed() {
                // Given
                Product product = Product.builder()
                    .id(TEST_PRODUCT_ID)
                    .title("테스트 상품")
                    .build();
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(product));
                when(feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
                    TEST_USER_ID, TEST_PRODUCT_ID, FeedStatus.PENDING))
                    .thenReturn(Optional.empty());
                when(dateTimeUtil.now()).thenReturn(TEST_DATE);

                Feed savedFeed = Feed.builder()
                    .id(TEST_FEED_ID)
                    .userId(TEST_USER_ID)
                    .productId(TEST_PRODUCT_ID)
                    .content(TEST_CONTENT)
                    .feedImages(List.of())
                    .grade(TEST_GRADE)
                    .price(TEST_PRICE)
                    .quantity(TEST_QUANTITY)
                    .status(FeedStatus.PENDING)
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                when(feedRepository.save(any(Feed.class))).thenReturn(savedFeed);

                CreateFeedRequest request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );

                // When & Then
                feedService.create(TEST_USER_ID, request);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 ID가 주어지면")
        class WithNonExistingProductId {

            @Test
            @DisplayName("존재하지 않는 상품 ID가 주어지면 NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // Given
                CreateFeedRequest request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> feedService.create(TEST_USER_ID, request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 상품입니다. productId: " + TEST_PRODUCT_ID);
            }
        }

        @Nested
        @DisplayName("이미 존재하는 피드가 있으면")
        class WithExistingFeed {

            @Test
            @DisplayName("이미 존재하는 피드가 있으면 FeedAlreadyExistsException을 발생시킨다")
            void throwsFeedAlreadyExistsException() {
                // Given
                Product product = Product.builder()
                    .id(TEST_PRODUCT_ID)
                    .title("테스트 상품")
                    .build();
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(product));
                when(feedRepository.findByUserIdAndProductIdAndFeedStatusAndFeedType(
                    TEST_USER_ID, TEST_PRODUCT_ID, FeedStatus.PENDING))
                    .thenReturn(Optional.of(Feed.builder().build()));

                CreateFeedRequest request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );

                // When & Then
                assertThatThrownBy(() -> feedService.create(TEST_USER_ID, request))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("이미 등록된 피드가 존재합니다. userId: " + TEST_USER_ID + ", productId: "
                        + TEST_PRODUCT_ID);
            }
        }
    }
}
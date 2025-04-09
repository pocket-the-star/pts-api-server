package com.pts.api.feed.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("FeedApplicationService 클래스")
class ReadFeedApplicationServiceTest extends BaseUnitTest {

    @Mock
    private FeedRepositoryPort feedRepository;

    private ReadFeedApplicationService feedService;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final String TEST_CONTENT = "테스트 피드 내용";
    private static final ProductGrade TEST_GRADE = ProductGrade.A;
    private static final Integer TEST_PRICE = 10000;
    private static final Integer TEST_QUANTITY = 10;
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        feedService = new ReadFeedApplicationService(feedRepository);
    }

    @Nested
    @DisplayName("findByUserId 메서드 호출 시")
    class DescribeFindByUserId {

        @Test
        @DisplayName("사용자의 피드 목록을 반환한다")
        void itReturnsUserFeeds() {
            // Given
            Feed feed = Feed.builder()
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
            when(feedRepository.findByUserIdAndDeletedAtIsNull(TEST_USER_ID, null, 20))
                .thenReturn(List.of(feed));

            // When
            List<MyFeedResponse> responses = feedService.getMyFeeds(TEST_USER_ID, null, 20);

            // Then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).id()).isEqualTo(TEST_FEED_ID);
            assertThat(responses.get(0).content()).isEqualTo(TEST_CONTENT);
            assertThat(responses.get(0).grade()).isEqualTo(TEST_GRADE);
            assertThat(responses.get(0).price()).isEqualTo(TEST_PRICE);
            assertThat(responses.get(0).quantity()).isEqualTo(TEST_QUANTITY);
        }
    }

    @Nested
    @DisplayName("findAll 메서드 호출 시")
    class DescribeFindAll {

        @Test
        @DisplayName("모든 피드 목록을 반환한다")
        void itReturnsAllFeeds() {
            // Given
            Feed feed = Feed.builder()
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
            when(feedRepository.findByDeletedAtIsNull(null, 20))
                .thenReturn(List.of(feed));

            // When
            List<FeedResponse> responses = feedService.getFeeds(null, 20);

            // Then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).id()).isEqualTo(TEST_FEED_ID);
            assertThat(responses.get(0).content()).isEqualTo(TEST_CONTENT);
            assertThat(responses.get(0).grade()).isEqualTo(TEST_GRADE);
            assertThat(responses.get(0).price()).isEqualTo(TEST_PRICE);
            assertThat(responses.get(0).quantity()).isEqualTo(TEST_QUANTITY);
        }
    }
} 
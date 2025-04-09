package com.pts.api.feed.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("UpdateFeedApplicationService 클래스")
class UpdateFeedApplicationServiceTest extends BaseUnitTest {

    @Mock
    private FeedRepositoryPort feedRepository;
    @Mock
    private DateTimeUtil dateTimeUtil;

    private UpdateFeedApplicationService feedService;

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
        feedService = new UpdateFeedApplicationService(feedRepository, dateTimeUtil);
    }

    @Nested
    @DisplayName("update 메서드 호출 시")
    class DescribeUpdate {

        @Nested
        @DisplayName("존재하는 피드 ID가 주어지면")
        class WithExistingFeedId {

            @Test
            @DisplayName("피드를 수정한다")
            void itUpdatesFeed() {
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
                when(feedRepository.findOneById(TEST_FEED_ID))
                    .thenReturn(Optional.of(feed));
                when(dateTimeUtil.now()).thenReturn(TEST_DATE);

                UpdateFeedRequest request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );

                // When & Then
                feedService.update(TEST_USER_ID, TEST_FEED_ID, request);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 피드 ID가 주어지면")
        class WithNonExistingFeedId {

            @Test
            @DisplayName("존재하지 않는 피드 ID가 주어지면 NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // Given
                UpdateFeedRequest request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                when(feedRepository.findOneById(TEST_FEED_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> feedService.update(TEST_USER_ID, TEST_FEED_ID, request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 피드입니다. id=" + TEST_FEED_ID);
            }
        }

        @Nested
        @DisplayName("다른 사용자의 피드 ID가 주어지면")
        class WithOtherUserFeedId {

            @Test
            @DisplayName("다른 사용자의 피드 ID가 주어지면 UnauthorizedException을 발생시킨다")
            void throwsUnauthorizedException() {
                // Given
                Feed feed = Feed.builder()
                    .id(TEST_FEED_ID)
                    .userId(2L)
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
                when(feedRepository.findOneById(TEST_FEED_ID))
                    .thenReturn(Optional.of(feed));

                UpdateFeedRequest request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );

                // When & Then
                assertThatThrownBy(() -> feedService.update(TEST_USER_ID, TEST_FEED_ID, request))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage(
                        "수정 권한이 없는 피드입니다. feedId: " + TEST_FEED_ID + ", userId: " + TEST_USER_ID);
            }
        }
    }
}
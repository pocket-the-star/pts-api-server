package com.pts.api.feed.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
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

@DisplayName("DeleteApplicationService 클래스")
class DeleteFeedApplicationServiceTest extends BaseUnitTest {

    @Mock
    private FeedRepositoryPort feedRepository;
    @Mock
    private DateTimeUtil dateTimeUtil;

    private DeleteFeedApplicationService feedService;

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
        feedService = new DeleteFeedApplicationService(feedRepository, dateTimeUtil);
    }

    @Nested
    @DisplayName("delete 메서드 호출 시")
    class DescribeDelete {

        @Nested
        @DisplayName("존재하는 피드 ID가 주어지면")
        class WithExistingFeedId {

            @Test
            @DisplayName("피드를 삭제한다")
            void itDeletesFeed() {
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

                // When & Then
                feedService.delete(TEST_USER_ID, TEST_FEED_ID);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 피드 ID가 주어지면")
        class WithNonExistingFeedId {

            @Test
            @DisplayName("존재하지 않는 피드 ID가 주어지면 NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // Given
                when(feedRepository.findOneById(TEST_FEED_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> feedService.delete(TEST_USER_ID, TEST_FEED_ID))
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

                // When & Then
                assertThatThrownBy(() -> feedService.delete(TEST_USER_ID, TEST_FEED_ID))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage(
                        "삭제 권한이 없는 피드입니다. feedId: " + TEST_FEED_ID + ", userId: " + TEST_USER_ID);
            }
        }
    }
}
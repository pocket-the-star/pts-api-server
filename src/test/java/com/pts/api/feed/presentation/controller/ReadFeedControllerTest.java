package com.pts.api.feed.presentation.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.ReadFeedUseCase;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("FeedController 클래스")
class ReadFeedControllerTest extends BaseIntegrationTest {

    @Autowired
    private ReadFeedUseCase readFeedUseCase;

    private static final String BASE_URL = "/api/v1";
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
        reset(readFeedUseCase);
    }

    @Nested
    @DisplayName("getMyFeeds 메서드 호출 시")
    class DescribeFindByUserId {

        private static final String URL = BASE_URL + "/private/feeds/my";

        @Test
        @DisplayName("200 OK와 함께 성공 응답을 반환한다")
        void returnsOkResponse() throws Exception {
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
            when(readFeedUseCase.getMyFeeds(TEST_USER_ID, null, 20))
                .thenReturn(List.of(MyFeedResponse.from(feed)));

            // When & Then
            mockMvc.perform(
                    get(URL)
                        .with(user("user").roles("USER"))
                        .param("lastId", "")
                        .param("limit", "20")
                )
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getFeeds 메서드 호출 시")
    class DescribeFindAll {

        private static final String URL = BASE_URL + "/feeds";

        @Test
        @DisplayName("200 OK와 함께 성공 응답을 반환한다")
        void returnsOkResponse() throws Exception {
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
            when(readFeedUseCase.getFeeds(null, 20))
                .thenReturn(List.of(FeedResponse.from(feed)));

            // When & Then
            mockMvc.perform(
                    get(URL)
                        .with(user("user").roles("USER"))
                        .param("lastFeedId", "1")
                        .param("limit", "20")
                )
                .andExpect(status().isOk());
        }
    }
} 
package com.pts.api.feed.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.dto.response.FeedResponse;
import com.pts.api.feed.application.dto.response.MyFeedResponse;
import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.feed.application.port.in.PostFeedUseCase;
import com.pts.api.feed.application.port.in.ReadFeedUseCase;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.enums.FeedStatus;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import com.pts.api.user.common.exception.AlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("FeedController 클래스")
class FeedControllerTest extends BaseIntegrationTest {

    @Autowired
    private PostFeedUseCase postFeedUseCase;
    @Autowired
    private UpdateFeedUseCase updateFeedUseCase;
    @Autowired
    private DeleteFeedUseCase deleteFeedUseCase;
    @Autowired
    private ReadFeedUseCase readFeedUseCase;

    private static final String BASE_URL = "/api/v1/feeds";
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
        reset(readFeedUseCase);
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        private static final String URL = BASE_URL;

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            private CreateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doNothing().when(postFeedUseCase).create(TEST_USER_ID, request);
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 ID가 주어지면")
        class WithNonExistingProductId {

            private CreateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doThrow(new NotFoundException("존재하지 않는 상품입니다. productId: " + TEST_PRODUCT_ID))
                    .when(postFeedUseCase).create(any(), any());
            }

            @Test
            @DisplayName("404 Not Found와 함께 에러 응답을 반환한다")
            void returnsNotFoundResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("이미 존재하는 피드가 있으면")
        class WithExistingFeed {

            private CreateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new CreateFeedRequest(
                    TEST_PRODUCT_ID,
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doThrow(new AlreadyExistsException(
                    "이미 등록된 피드가 존재합니다. userId: " + TEST_USER_ID + ", productId: "
                        + TEST_PRODUCT_ID))
                    .when(postFeedUseCase).create(any(), any());
            }

            @Test
            @DisplayName("409 Conflict와 함께 에러 응답을 반환한다")
            void returnsConflictResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isConflict());
            }
        }
    }

    @Nested
    @DisplayName("update 메서드 호출 시")
    class DescribeUpdate {

        private static final String URL = BASE_URL + "/{feedId}";

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            private UpdateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doNothing().when(updateFeedUseCase).update(any(), any(), any());
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        put(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 피드 ID가 주어지면")
        class WithNonExistingFeedId {

            private UpdateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doThrow(new NotFoundException("존재하지 않는 피드입니다. id=" + TEST_FEED_ID))
                    .when(updateFeedUseCase).update(any(), any(), any());
            }

            @Test
            @DisplayName("404 Not Found와 함께 에러 응답을 반환한다")
            void returnsNotFoundResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        put(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("다른 사용자의 피드 ID가 주어지면")
        class WithOtherUserFeedId {

            private UpdateFeedRequest request;

            @BeforeEach
            void setUp() {
                request = new UpdateFeedRequest(
                    TEST_CONTENT,
                    TEST_IMAGE_URLS,
                    TEST_GRADE,
                    TEST_PRICE,
                    TEST_QUANTITY
                );
                doThrow(new UnAuthorizedException("권한이 없습니다."))
                    .when(updateFeedUseCase).update(any(), any(), any());
            }

            @Test
            @DisplayName("401 Unauthorized와 함께 에러 응답을 반환한다")
            void returnsUnauthorizedResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        put(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드 호출 시")
    class DescribeDelete {

        private static final String URL = BASE_URL + "/{feedId}";

        @Nested
        @DisplayName("존재하는 피드 ID가 주어지면")
        class WithExistingFeedId {

            @BeforeEach
            void setUp() {
                doNothing().when(deleteFeedUseCase).delete(any(), any());
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        delete(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 피드 ID가 주어지면")
        class WithNonExistingFeedId {

            @BeforeEach
            void setUp() {
                doThrow(new NotFoundException("존재하지 않는 피드입니다. id=" + TEST_FEED_ID))
                    .when(deleteFeedUseCase).delete(any(), any());
            }

            @Test
            @DisplayName("404 Not Found와 함께 에러 응답을 반환한다")
            void returnsNotFoundResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        delete(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("다른 사용자의 피드 ID가 주어지면")
        class WithOtherUserFeedId {

            @BeforeEach
            void setUp() {
                doThrow(new UnAuthorizedException("권한이 없습니다."))
                    .when(deleteFeedUseCase).delete(any(), any());
            }

            @Test
            @DisplayName("401 Unauthorized와 함께 에러 응답을 반환한다")
            void returnsUnauthorizedResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        delete(URL, TEST_FEED_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("findByUserId 메서드 호출 시")
    class DescribeFindByUserId {

        private static final String URL = BASE_URL + "/my";

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
    @DisplayName("findAll 메서드 호출 시")
    class DescribeFindAll {

        private static final String URL = BASE_URL;

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
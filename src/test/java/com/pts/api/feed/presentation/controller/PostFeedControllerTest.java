package com.pts.api.feed.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.feed.application.dto.request.CreateFeedRequest;
import com.pts.api.feed.application.port.in.PostFeedUseCase;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import com.pts.api.user.common.exception.AlreadyExistsException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("PostFeedController 클래스")
class PostFeedControllerTest extends BaseIntegrationTest {

    @Autowired
    private PostFeedUseCase postFeedUseCase;

    private static final String BASE_URL = "/api/v1/private/feeds";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final String TEST_CONTENT = "테스트 피드 내용";
    private static final List<String> TEST_IMAGE_URLS = List.of("test1.jpg", "test2.jpg");
    private static final ProductGrade TEST_GRADE = ProductGrade.A;
    private static final Integer TEST_PRICE = 10000;
    private static final Integer TEST_QUANTITY = 10;

    @BeforeEach
    void setUp() {
        reset(postFeedUseCase);
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
}
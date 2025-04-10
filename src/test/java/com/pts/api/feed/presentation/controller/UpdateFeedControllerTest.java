package com.pts.api.feed.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.feed.application.dto.request.UpdateFeedRequest;
import com.pts.api.feed.application.port.in.UpdateFeedUseCase;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.enums.ProductGrade;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("UpdateFeedController 클래스")
class UpdateFeedControllerTest extends BaseIntegrationTest {

    @Autowired
    private UpdateFeedUseCase updateFeedUseCase;


    private static final String BASE_URL = "/api/v1/private/feeds";
    private static final Long TEST_FEED_ID = 1L;
    private static final String TEST_CONTENT = "테스트 피드 내용";
    private static final List<String> TEST_IMAGE_URLS = List.of("test1.jpg", "test2.jpg");
    private static final ProductGrade TEST_GRADE = ProductGrade.A;
    private static final Integer TEST_PRICE = 10000;
    private static final Integer TEST_QUANTITY = 10;

    @BeforeEach
    void setUp() {
        reset(updateFeedUseCase);
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
}
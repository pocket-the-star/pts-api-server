package com.pts.api.feed.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.feed.application.port.in.DeleteFeedUseCase;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("DeleteFeedController 클래스")
class DeleteFeedControllerTest extends BaseIntegrationTest {

    @Autowired
    private DeleteFeedUseCase deleteFeedUseCase;

    private static final String BASE_URL = "/api/v1/private/feeds";
    private static final Long TEST_FEED_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(deleteFeedUseCase);
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
}
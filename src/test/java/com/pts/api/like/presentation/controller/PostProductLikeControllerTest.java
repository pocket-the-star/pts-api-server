package com.pts.api.like.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.application.port.in.PostProductLikeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductLikeController 클래스")
class PostProductLikeControllerTest extends BaseIntegrationTest {

    @Autowired
    private PostProductLikeUseCase postProductLikeUseCase;

    private static final String BASE_URL = "/api/v1/private/products/{productId}/likes";
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(postProductLikeUseCase);
    }


    @Nested
    @DisplayName("post 메서드 호출 시")
    class DescribePost {

        private static final String URL = BASE_URL;

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            @BeforeEach
            void setUp() {
                doNothing().when(postProductLikeUseCase).like(any(), any());
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL, TEST_PRODUCT_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 ID가 주어지면")
        class WithNonExistingProductId {

            @BeforeEach
            void setUp() {
                doThrow(new NotFoundException("존재하지 않는 상품입니다.: " + TEST_PRODUCT_ID))
                    .when(postProductLikeUseCase).like(any(), any());
            }

            @Test
            @DisplayName("404 Not Found와 함께 에러 응답을 반환한다")
            void returnsNotFoundResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL, TEST_PRODUCT_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("이미 좋아요한 상품이면")
        class WithAlreadyLikedProduct {

            @BeforeEach
            void setUp() {
                doThrow(new AlreadyExistException("이미 좋아요한 상품입니다.: " + TEST_PRODUCT_ID))
                    .when(postProductLikeUseCase).like(any(), any());
            }

            @Test
            @DisplayName("409 Conflict와 함께 에러 응답을 반환한다")
            void returnsConflictResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        post(URL, TEST_PRODUCT_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isConflict());
            }
        }
    }
} 
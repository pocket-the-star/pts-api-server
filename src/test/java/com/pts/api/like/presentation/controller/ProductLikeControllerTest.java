package com.pts.api.like.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.application.port.in.ProductLikeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductLikeController 클래스")
class ProductLikeControllerTest extends BaseIntegrationTest {

    @Autowired
    private ProductLikeUseCase productLikeUseCase;

    private static final String BASE_URL = "/api/v1/products/{productId}/likes";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(productLikeUseCase);
    }

    @Nested
    @DisplayName("getLikeStatus 메서드 호출 시")
    class DescribeGetLikeStatus {

        private static final String URL = BASE_URL;

        @Test
        @DisplayName("200 OK와 함께 성공 응답을 반환한다")
        void returnsOkResponse() throws Exception {
            // Given
            when(productLikeUseCase.isLiked(TEST_PRODUCT_ID, TEST_USER_ID))
                .thenReturn(true);

            // When & Then
            mockMvc.perform(
                    get(URL, TEST_PRODUCT_ID)
                        .with(user("user").roles("USER"))
                    )
                .andExpect(status().isOk());
        }
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
                doNothing().when(productLikeUseCase).like(any(), any());
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
                    .when(productLikeUseCase).like(any(), any());
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
                    .when(productLikeUseCase).like(any(), any());
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

    @Nested
    @DisplayName("delete 메서드 호출 시")
    class DescribeDelete {

        private static final String URL = BASE_URL;

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            @BeforeEach
            void setUp() {
                doNothing().when(productLikeUseCase).unlike(any(), any());
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        delete(URL, TEST_PRODUCT_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("좋아요하지 않은 상품이면")
        class WithNotLikedProduct {

            @BeforeEach
            void setUp() {
                doThrow(new NotFoundException("좋아요하지 않은 상품입니다.: " + TEST_PRODUCT_ID))
                    .when(productLikeUseCase).unlike(any(), any());
            }

            @Test
            @DisplayName("404 Not Found와 함께 에러 응답을 반환한다")
            void returnsNotFoundResponse() throws Exception {
                // When & Then
                mockMvc.perform(
                        delete(URL, TEST_PRODUCT_ID)
                            .with(user("user").roles("USER"))
                    )
                    .andExpect(status().isNotFound());
            }
        }
    }
} 
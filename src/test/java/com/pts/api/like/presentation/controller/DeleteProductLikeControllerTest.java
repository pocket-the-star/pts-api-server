package com.pts.api.like.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.application.port.in.DeleteProductLikeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("DeleteProductLikeController 클래스")
class DeleteProductLikeControllerTest extends BaseIntegrationTest {

    @Autowired
    private DeleteProductLikeUseCase deleteProductLikeUseCase;

    private static final String BASE_URL = "/api/v1/private/products/{productId}/likes";
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        reset(deleteProductLikeUseCase);
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
                doNothing().when(deleteProductLikeUseCase).unlike(any(), any());
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
                    .when(deleteProductLikeUseCase).unlike(any(), any());
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
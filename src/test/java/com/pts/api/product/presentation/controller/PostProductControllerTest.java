package com.pts.api.product.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.product.application.dto.request.CreateProductRequest;
import com.pts.api.product.application.port.in.PostProductUseCase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("ProductController 클래스")
class PostProductControllerTest extends BaseIntegrationTest {

    @Autowired
    private PostProductUseCase postProductUseCase;

    private static final String BASE_URL = "/api/v1/products";
    private static final Long TEST_ID = 1L;
    private static final String TEST_TITLE = "테스트 상품";
    private static final Long TEST_SUB_CATEGORY_ID = 1L;
    private static final Long TEST_ARTIST_ID = 1L;
    private static final Integer TEST_MIN_BUY_PRICE = 10000;
    private static final Integer TEST_MAX_SELL_PRICE = 20000;
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        reset(postProductUseCase);
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // Given
            CreateProductRequest request = new CreateProductRequest(
                TEST_ID,
                TEST_TITLE,
                TEST_SUB_CATEGORY_ID,
                TEST_ARTIST_ID,
                List.of("image1.jpg", "image2.jpg")
            );

            doNothing().when(postProductUseCase).create(any(CreateProductRequest.class));

            // When & Then
            ResultActions result = mockMvc.perform(post(BASE_URL)
                .with(user("testUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

            result.andExpect(status().isOk());
        }
    }
} 
package com.pts.api.product.presentation.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.product.application.dto.response.ProductImageResponse;
import com.pts.api.product.application.dto.response.ProductResponse;
import com.pts.api.product.application.port.in.ReadProductUseCase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("ReadProductController 클래스")
class ReadProductControllerTest extends BaseIntegrationTest {

    @Autowired
    private ReadProductUseCase readProductUseCase;

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
        reset(readProductUseCase);
    }

    @Nested
    @DisplayName("findById 메서드 호출 시")
    class DescribeFindById {

        @Test
        @DisplayName("유효한 요청이면 200 OK와 함께 상품 정보를 반환한다")
        void returns200OkWithProduct() throws Exception {
            // Given
            ProductResponse response = new ProductResponse(
                TEST_ID,
                TEST_TITLE,
                TEST_SUB_CATEGORY_ID,
                TEST_ARTIST_ID,
                TEST_MIN_BUY_PRICE,
                TEST_MAX_SELL_PRICE,
                List.of(new ProductImageResponse(
                    TEST_ID,
                    "image1.jpg",
                    0,
                    TEST_DATE,
                    TEST_DATE
                )),
                TEST_DATE,
                TEST_DATE
            );

            when(readProductUseCase.findById(TEST_ID)).thenReturn(response);

            // When & Then
            ResultActions result = mockMvc.perform(get(BASE_URL + "/{id}", TEST_ID)
                .with(user("testUser").roles("USER")));

            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(TEST_ID))
                .andExpect(jsonPath("$.data.title").value(TEST_TITLE))
                .andExpect(jsonPath("$.data.subCategoryId").value(TEST_SUB_CATEGORY_ID))
                .andExpect(jsonPath("$.data.artistId").value(TEST_ARTIST_ID))
                .andExpect(jsonPath("$.data.minBuyPrice").value(TEST_MIN_BUY_PRICE))
                .andExpect(jsonPath("$.data.maxSellPrice").value(TEST_MAX_SELL_PRICE));
        }
    }

    @Nested
    @DisplayName("findAll 메서드 호출 시")
    class DescribeFindAll {

        @Test
        @DisplayName("유효한 요청이면 200 OK와 함께 상품 목록을 반환한다")
        void returns200OkWithProducts() throws Exception {
            // Given
            ProductResponse product1 = new ProductResponse(
                TEST_ID,
                TEST_TITLE,
                TEST_SUB_CATEGORY_ID,
                TEST_ARTIST_ID,
                TEST_MIN_BUY_PRICE,
                TEST_MAX_SELL_PRICE,
                List.of(new ProductImageResponse(
                    TEST_ID,
                    "image1.jpg",
                    0,
                    TEST_DATE,
                    TEST_DATE
                )),
                TEST_DATE,
                TEST_DATE
            );

            ProductResponse product2 = new ProductResponse(
                2L,
                "테스트 상품 2",
                TEST_SUB_CATEGORY_ID,
                TEST_ARTIST_ID,
                TEST_MIN_BUY_PRICE,
                TEST_MAX_SELL_PRICE,
                List.of(new ProductImageResponse(
                    2L,
                    "image2.jpg",
                    0,
                    TEST_DATE,
                    TEST_DATE
                )),
                TEST_DATE,
                TEST_DATE
            );

            when(readProductUseCase.findAll(null, null, null, 0L, 20))
                .thenReturn(List.of(product1, product2));

            // When & Then
            ResultActions result = mockMvc.perform(get(BASE_URL)
                .with(user("testUser").roles("USER")));

            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(TEST_ID))
                .andExpect(jsonPath("$.data[0].title").value(TEST_TITLE))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].title").value("테스트 상품 2"));
        }
    }
}
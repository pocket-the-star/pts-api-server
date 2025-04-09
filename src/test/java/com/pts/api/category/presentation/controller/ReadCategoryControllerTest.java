package com.pts.api.category.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import com.pts.api.category.application.service.ReadCategoryService;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import com.pts.api.common.base.BaseIntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("CategoryController 클래스")
class ReadCategoryControllerTest extends BaseIntegrationTest {

    @Autowired
    private ReadCategoryService readCategoryService;

    private static final Long TEST_ID_1 = 1L;
    private static final Long TEST_ID_2 = 2L;
    private static final String TEST_NAME = "테스트 카테고리";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        reset(readCategoryService);
    }

    @Nested
    @DisplayName("getCategories 메서드 호출 시")
    class DescribeGetCategories {

        @Test
        @DisplayName("모든 카테고리 목록을 반환한다")
        void itReturnsAllCategories() throws Exception {
            // Given
            ReadCategoryResponse category1 = new ReadCategoryResponse(TEST_ID_1, TEST_NAME,
                TEST_DATE, TEST_DATE);
            ReadCategoryResponse category2 = new ReadCategoryResponse(TEST_ID_2, "테스트 카테고리 2",
                TEST_DATE, TEST_DATE);
            given(readCategoryService.getCategories()).willReturn(List.of(category1, category2));

            // When
            ResultActions result = mockMvc.perform(get("/api/v1/categories")
                .with(user("testUser").roles("USER")));

            // Then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getCategory 메서드 호출 시")
    class DescribeGetCategory {

        @Nested
        @DisplayName("존재하는 카테고리 ID가 주어지면")
        class WithExistingCategoryId {

            @Test
            @DisplayName("카테고리 정보를 반환한다")
            void itReturnsCategory() throws Exception {
                // Given
                ReadCategoryResponse category = new ReadCategoryResponse(TEST_ID_1, TEST_NAME,
                    TEST_DATE, TEST_DATE);
                given(readCategoryService.getCategory(TEST_ID_1)).willReturn(category);

                // When
                ResultActions result = mockMvc.perform(get("/api/v1/categories/{id}", TEST_ID_1)
                    .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 카테고리 ID가 주어지면")
        class WithNonExistingCategoryId {

            @Test
            @DisplayName("404 에러를 반환한다")
            void itReturns404Error() throws Exception {
                // Given
                given(readCategoryService.getCategory(TEST_ID_2))
                    .willThrow(
                        new CategoryNotFoundException("Category not found with id: " + TEST_ID_2));

                // When
                ResultActions result = mockMvc.perform(get("/api/v1/categories/{id}", TEST_ID_2)
                    .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isNotFound());
            }
        }
    }
} 
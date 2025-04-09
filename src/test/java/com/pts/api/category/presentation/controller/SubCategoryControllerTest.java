package com.pts.api.category.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import com.pts.api.category.application.service.SubCategoryApplicationService;
import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("SubCategoryController 클래스")
class SubCategoryControllerTest extends BaseIntegrationTest {

    @Autowired
    private SubCategoryApplicationService subCategoryApplicationService;

    private static final Long TEST_CATEGORY_ID = 1L;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 서브 카테고리";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        reset(subCategoryApplicationService);
    }

    @Nested
    @DisplayName("getSubCategories 메서드 호출 시")
    class DescribeGetSubCategories {

        @Test
        @DisplayName("모든 서브 카테고리 목록을 반환한다")
        void itReturnsAllSubCategories() throws Exception {
            // Given
            ReadSubCategoryResponse subCategory1 = new ReadSubCategoryResponse(TEST_ID, TEST_NAME,
                TEST_CATEGORY_ID, TEST_DATE, TEST_DATE);
            ReadSubCategoryResponse subCategory2 = new ReadSubCategoryResponse(2L, "테스트 서브 카테고리 2",
                TEST_CATEGORY_ID, TEST_DATE, TEST_DATE);
            given(subCategoryApplicationService.getSubCategories(TEST_CATEGORY_ID)).willReturn(
                List.of(subCategory1, subCategory2));

            // When
            ResultActions result = mockMvc.perform(
                get("/api/v1/categories/{categoryId}/sub-categories", TEST_CATEGORY_ID)
                    .with(user("testUser").roles("USER")));

            // Then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getSubCategory 메서드 호출 시")
    class DescribeGetSubCategory {

        @Nested
        @DisplayName("존재하는 서브 카테고리 ID가 주어지면")
        class WithExistingSubCategoryId {

            @Test
            @DisplayName("서브 카테고리 정보를 반환한다")
            void itReturnsSubCategory() throws Exception {
                // Given
                ReadSubCategoryResponse subCategory = new ReadSubCategoryResponse(TEST_ID,
                    TEST_NAME, TEST_CATEGORY_ID, TEST_DATE, TEST_DATE);
                given(subCategoryApplicationService.getSubCategory(TEST_CATEGORY_ID,
                    TEST_ID)).willReturn(
                    subCategory);

                // When
                ResultActions result = mockMvc.perform(
                    get("/api/v1/categories/{categoryId}/sub-categories/{id}", TEST_CATEGORY_ID,
                        TEST_ID)
                        .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 서브 카테고리 ID가 주어지면")
        class WithNonExistingSubCategoryId {

            @Test
            @DisplayName("404 에러를 반환한다")
            void itReturns404Error() throws Exception {
                // Given
                given(subCategoryApplicationService.getSubCategory(TEST_CATEGORY_ID, TEST_ID))
                    .willThrow(new NotFoundException("SubCategory not found with id: " + TEST_ID));

                // When
                ResultActions result = mockMvc.perform(
                    get("/api/v1/categories/{categoryId}/sub-categories/{id}", TEST_CATEGORY_ID,
                        TEST_ID)
                        .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isNotFound());
            }
        }
    }
} 
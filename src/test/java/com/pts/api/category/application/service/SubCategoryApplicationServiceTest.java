package com.pts.api.category.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import com.pts.api.category.application.port.out.SubCategoryRepositoryPort;
import com.pts.api.category.domain.model.SubCategory;
import com.pts.api.common.base.BaseUnitTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("SubCategoryService 클래스")
class SubCategoryApplicationServiceTest extends BaseUnitTest {

    @Mock
    private SubCategoryRepositoryPort subCategoryRepositoryPort;

    private SubCategoryApplicationService subCategoryApplicationService;

    private static final Long TEST_CATEGORY_ID = 1L;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 서브 카테고리";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        subCategoryApplicationService = new SubCategoryApplicationService(
            subCategoryRepositoryPort);
    }

    @Nested
    @DisplayName("getSubCategory 메서드 호출 시")
    class DescribeGetSubCategory {

        @Nested
        @DisplayName("존재하는 서브 카테고리 ID가 주어지면")
        class WithExistingSubCategoryId {

            @Test
            @DisplayName("서브 카테고리 정보를 반환한다")
            void itReturnsSubCategory() {
                // Given
                SubCategory subCategory = SubCategory.builder()
                    .id(TEST_ID)
                    .name(TEST_NAME)
                    .categoryId(TEST_CATEGORY_ID)
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                when(subCategoryRepositoryPort.findOneById(TEST_CATEGORY_ID, TEST_ID))
                    .thenReturn(Optional.of(subCategory));

                // When
                ReadSubCategoryResponse response = subCategoryApplicationService.getSubCategory(
                    TEST_CATEGORY_ID, TEST_ID);

                // Then
                assertThat(response.id()).isEqualTo(TEST_ID);
                assertThat(response.name()).isEqualTo(TEST_NAME);
                assertThat(response.categoryId()).isEqualTo(TEST_CATEGORY_ID);
                assertThat(response.createdAt()).isEqualTo(TEST_DATE);
                assertThat(response.updatedAt()).isEqualTo(TEST_DATE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 서브 카테고리 ID가 주어지면")
        class WithNonExistingSubCategoryId {

            @Test
            @DisplayName("RuntimeException이 발생한다")
            void itThrowsRuntimeException() {
                // Given
                when(subCategoryRepositoryPort.findOneById(TEST_CATEGORY_ID, TEST_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(
                    () -> subCategoryApplicationService.getSubCategory(TEST_CATEGORY_ID, TEST_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("SubCategory not found with id: " + TEST_ID);
            }
        }
    }

    @Nested
    @DisplayName("getSubCategories 메서드 호출 시")
    class DescribeGetSubCategories {

        @Test
        @DisplayName("모든 서브 카테고리 목록을 반환한다")
        void itReturnsAllSubCategories() {
            // Given
            SubCategory subCategory1 = SubCategory.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .categoryId(TEST_CATEGORY_ID)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            SubCategory subCategory2 = SubCategory.builder()
                .id(2L)
                .name("테스트 서브 카테고리 2")
                .categoryId(TEST_CATEGORY_ID)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            when(subCategoryRepositoryPort.findAllByCategoryId(TEST_CATEGORY_ID))
                .thenReturn(List.of(subCategory1, subCategory2));

            // When
            List<ReadSubCategoryResponse> responses = subCategoryApplicationService.getSubCategories(
                TEST_CATEGORY_ID);

            // Then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(TEST_ID);
            assertThat(responses.get(0).name()).isEqualTo(TEST_NAME);
            assertThat(responses.get(0).categoryId()).isEqualTo(TEST_CATEGORY_ID);
            assertThat(responses.get(1).id()).isEqualTo(2L);
            assertThat(responses.get(1).name()).isEqualTo("테스트 서브 카테고리 2");
            assertThat(responses.get(1).categoryId()).isEqualTo(TEST_CATEGORY_ID);
        }
    }
} 
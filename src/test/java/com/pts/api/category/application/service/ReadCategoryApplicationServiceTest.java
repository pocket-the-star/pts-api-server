package com.pts.api.category.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import com.pts.api.category.application.port.out.CategoryRepositoryPort;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import com.pts.api.category.domain.model.Category;
import com.pts.api.common.base.BaseUnitTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("CategoryService 클래스")
class ReadCategoryApplicationServiceTest extends BaseUnitTest {

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    private ReadCategoryApplicationService readCategoryApplicationService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 카테고리";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        readCategoryApplicationService = new ReadCategoryApplicationService(categoryRepositoryPort);
    }

    @Nested
    @DisplayName("getCategory 메서드 호출 시")
    class DescribeGetCategory {

        @Nested
        @DisplayName("존재하는 카테고리 ID가 주어지면")
        class WithExistingCategoryId {

            @Test
            @DisplayName("카테고리 정보를 반환한다")
            void itReturnsCategory() {
                // Given
                Category category = Category.builder()
                    .id(TEST_ID)
                    .name(TEST_NAME)
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                when(categoryRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.of(category));

                // When
                ReadCategoryResponse response = readCategoryApplicationService.getCategory(TEST_ID);

                // Then
                assertThat(response.id()).isEqualTo(TEST_ID);
                assertThat(response.name()).isEqualTo(TEST_NAME);
                assertThat(response.createdAt()).isEqualTo(TEST_DATE);
                assertThat(response.updatedAt()).isEqualTo(TEST_DATE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 카테고리 ID가 주어지면")
        class WithNonExistingCategoryId {

            @Test
            @DisplayName("CategoryNotFoundException이 발생한다")
            void itThrowsCategoryNotFoundException() {
                // Given
                when(categoryRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> readCategoryApplicationService.getCategory(TEST_ID))
                    .isInstanceOf(CategoryNotFoundException.class)
                    .hasMessage("Category not found with id: " + TEST_ID);
            }
        }
    }

    @Nested
    @DisplayName("getCategories 메서드 호출 시")
    class DescribeGetCategories {

        @Test
        @DisplayName("모든 카테고리 목록을 반환한다")
        void itReturnsAllCategories() {
            // Given
            Category category1 = Category.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            Category category2 = Category.builder()
                .id(2L)
                .name("테스트 카테고리 2")
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            when(categoryRepositoryPort.findAll())
                .thenReturn(List.of(category1, category2));

            // When
            List<ReadCategoryResponse> responses = readCategoryApplicationService.getCategories();

            // Then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(TEST_ID);
            assertThat(responses.get(0).name()).isEqualTo(TEST_NAME);
            assertThat(responses.get(1).id()).isEqualTo(2L);
            assertThat(responses.get(1).name()).isEqualTo("테스트 카테고리 2");
        }
    }
} 
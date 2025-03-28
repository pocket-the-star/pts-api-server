package com.pts.api.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.port.dto.response.ReadCategoryResponse;
import com.pts.api.category.application.service.CategoryService;
import com.pts.api.category.common.exception.CategoryNotFoundException;
import com.pts.api.category.infrastructure.persistence.entity.CategoryEntity;
import com.pts.api.category.infrastructure.persistence.repository.CategoryRepository;
import com.pts.api.common.base.BaseUnitTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetCategoryService 클래스")
class CategoryEntityServiceTest extends BaseUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;
    private final Long TEST_ID = 1L;
    private final String TEST_NAME = "CategoryName";
    private final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Given
        categoryService = new CategoryService(categoryRepository);
    }

    @Nested
    @DisplayName("getCategory 메서드 호출 시")
    class DescribeGetCategoryEntity {

        @Test
        @DisplayName("유효한 ID 조회 시 카테고리 DTO 반환")
        void itReturnsCategory() {
            // Given
            ReadCategoryResponse expected = new ReadCategoryResponse(TEST_ID, TEST_NAME,
                TEST_DATE, TEST_DATE);
            CategoryEntity categoryEntity = new CategoryEntity(TEST_ID, TEST_NAME, TEST_DATE,
                TEST_DATE, null);
            when(categoryRepository.findOneById(TEST_ID)).thenReturn(Optional.of(categoryEntity));

            // When
            ReadCategoryResponse actual = categoryService.getCategory(TEST_ID);

            // Then
            assertEquals(expected, actual);
            verify(categoryRepository, times(1)).findOneById(TEST_ID);
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 예외 발생")
        void itThrowsCategoryNotFoundException() {
            // Given
            when(categoryRepository.findOneById(TEST_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategory(TEST_ID));
            verify(categoryRepository, times(1)).findOneById(TEST_ID);
        }
    }

    @Nested
    @DisplayName("getCategories 메서드 호출 시")
    class DescribeGetCategories {

        @Test
        @DisplayName("전체 카테고리 리스트 반환")
        void itReturnsCategoriesList() {
            // Given
            List<ReadCategoryResponse> expectedList = List.of(
                new ReadCategoryResponse(TEST_ID, TEST_NAME, TEST_DATE, TEST_DATE),
                new ReadCategoryResponse(TEST_ID + 1, TEST_NAME + "1", TEST_DATE, TEST_DATE)
            );
            List<CategoryEntity> categories = List.of(
                new CategoryEntity(TEST_ID, TEST_NAME, TEST_DATE, TEST_DATE, null),
                new CategoryEntity(TEST_ID + 1, TEST_NAME + "1", TEST_DATE, TEST_DATE, null)
            );
            when(categoryRepository.findAll()).thenReturn(categories);
            // When
            List<ReadCategoryResponse> actualList = categoryService.getCategories();
            // Then
            assertEquals(expectedList, actualList);
            verify(categoryRepository, times(1)).findAll();
        }
    }
}
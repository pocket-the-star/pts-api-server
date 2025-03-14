package com.pts.api.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.category.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.model.SubCategory;
import com.pts.api.category.repository.SubCategoryRepository;
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
@DisplayName("GetSubCategoryService 클래스")
class SubCategoryServiceTest extends BaseUnitTest {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    private SubCategoryService subCategoryService;
    private final Long CATEGORY_ID = 10L;
    private final Long SUBCATEGORY_ID = 1L;
    private final String SUBCATEGORY_NAME = "SubCategoryName";
    private final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Given
        subCategoryService = new SubCategoryService(subCategoryRepository);
    }

    @Nested
    @DisplayName("getSubCategory 메서드 호출 시")
    class DescribeGetSubCategory {

        @Test
        @DisplayName("유효한 categoryId와 id로 조회 시 DTO 반환")
        void itReturnsSubCategory() {
            // Given
            GetSubCategoryResponseDto expected = new GetSubCategoryResponseDto(SUBCATEGORY_ID,
                "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE);
            SubCategory subCategory = new SubCategory(SUBCATEGORY_ID, SUBCATEGORY_NAME, CATEGORY_ID,
                TEST_DATE, TEST_DATE, null);
            when(subCategoryRepository.findOneById(CATEGORY_ID, SUBCATEGORY_ID)).thenReturn(
                Optional.of(subCategory));

            // When
            GetSubCategoryResponseDto actual = subCategoryService.getSubCategory(CATEGORY_ID,
                SUBCATEGORY_ID);

            // Then
            assertEquals(expected, actual);
            verify(subCategoryRepository, times(1)).findOneById(CATEGORY_ID, SUBCATEGORY_ID);
        }

        @Test
        @DisplayName("존재하지 않는 id 조회 시 예외 발생")
        void itThrowsExceptionWhenNotFound() {
            // Given
            when(subCategoryRepository.findOneById(CATEGORY_ID, SUBCATEGORY_ID)).thenReturn(
                Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class,
                () -> subCategoryService.getSubCategory(CATEGORY_ID, SUBCATEGORY_ID));
            verify(subCategoryRepository, times(1)).findOneById(CATEGORY_ID, SUBCATEGORY_ID);
        }
    }

    @Nested
    @DisplayName("getSubCategories 메서드 호출 시")
    class DescribeGetSubCategories {

        @Test
        @DisplayName("해당 categoryId의 전체 SubCategory 리스트 반환")
        void itReturnsSubCategoryList() {
            // Given
            List<GetSubCategoryResponseDto> expectedList = List.of(
                new GetSubCategoryResponseDto(1L, "SubCategoryName", CATEGORY_ID, TEST_DATE,
                    TEST_DATE),
                new GetSubCategoryResponseDto(2L, "SubCategoryName", CATEGORY_ID, TEST_DATE,
                    TEST_DATE));
            List<SubCategory> subCategories = List.of(
                new SubCategory(1L, "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE, null),
                new SubCategory(2L, "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE, null));
            when(subCategoryRepository.findAllByCategoryId(CATEGORY_ID)).thenReturn(subCategories);

            // When
            List<GetSubCategoryResponseDto> actualList = subCategoryService.getSubCategories(
                CATEGORY_ID);

            // Then
            assertEquals(expectedList, actualList);
            verify(subCategoryRepository, times(1)).findAllByCategoryId(CATEGORY_ID);
        }
    }
}
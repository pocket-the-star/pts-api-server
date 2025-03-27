package com.pts.api.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.port.dto.response.ReadSubCategoryResponse;
import com.pts.api.category.application.service.SubCategoryService;
import com.pts.api.category.infrastructure.persistence.entity.SubCategoryEntity;
import com.pts.api.category.infrastructure.persistence.repository.SubCategoryRepository;
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
class SubCategoryEntityEntityServiceTest extends BaseUnitTest {

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
    class DescribeGetSubCategoryEntityEntity {

        @Test
        @DisplayName("유효한 categoryId와 id로 조회 시 DTO 반환")
        void itReturnsSubCategory() {
            // Given
            ReadSubCategoryResponse expected = new ReadSubCategoryResponse(SUBCATEGORY_ID,
                "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE);
            SubCategoryEntity subCategoryEntity = new SubCategoryEntity(SUBCATEGORY_ID,
                SUBCATEGORY_NAME, CATEGORY_ID,
                TEST_DATE, TEST_DATE, null);
            when(subCategoryRepository.findOneById(CATEGORY_ID, SUBCATEGORY_ID)).thenReturn(
                Optional.of(subCategoryEntity));

            // When
            ReadSubCategoryResponse actual = subCategoryService.getSubCategory(CATEGORY_ID,
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
            List<ReadSubCategoryResponse> expectedList = List.of(
                new ReadSubCategoryResponse(1L, "SubCategoryName", CATEGORY_ID, TEST_DATE,
                    TEST_DATE),
                new ReadSubCategoryResponse(2L, "SubCategoryName", CATEGORY_ID, TEST_DATE,
                    TEST_DATE));
            List<SubCategoryEntity> subCategories = List.of(
                new SubCategoryEntity(1L, "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE,
                    null),
                new SubCategoryEntity(2L, "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE,
                    null));
            when(subCategoryRepository.findAllByCategoryId(CATEGORY_ID)).thenReturn(subCategories);

            // When
            List<ReadSubCategoryResponse> actualList = subCategoryService.getSubCategories(
                CATEGORY_ID);

            // Then
            assertEquals(expectedList, actualList);
            verify(subCategoryRepository, times(1)).findAllByCategoryId(CATEGORY_ID);
        }
    }
}
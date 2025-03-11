package com.pts.api.unit.category.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import com.pts.api.category.application.port.out.ReadSubCategoryRepositoryPort;
import com.pts.api.category.application.service.GetSubCategoryService;
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
class GetSubCategoryServiceTest extends BaseUnitTest {

    @Mock
    private ReadSubCategoryRepositoryPort readSubCategoryRepositoryPort;

    private GetSubCategoryService getSubCategoryService;
    private final Long CATEGORY_ID = 10L;
    private final Long SUBCATEGORY_ID = 1L;
    private final String SUBCATEGORY_NAME = "SubCategoryName";
    private final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Given
        getSubCategoryService = new GetSubCategoryService(readSubCategoryRepositoryPort);
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
            when(readSubCategoryRepositoryPort.findOneById(CATEGORY_ID, SUBCATEGORY_ID))
                .thenReturn(Optional.of(expected));

            // When
            GetSubCategoryResponseDto actual = getSubCategoryService.getSubCategory(CATEGORY_ID,
                SUBCATEGORY_ID);

            // Then
            assertEquals(expected, actual);
            verify(readSubCategoryRepositoryPort, times(1)).findOneById(CATEGORY_ID,
                SUBCATEGORY_ID);
        }

        @Test
        @DisplayName("존재하지 않는 id 조회 시 예외 발생")
        void itThrowsExceptionWhenNotFound() {
            // Given
            when(readSubCategoryRepositoryPort.findOneById(CATEGORY_ID, SUBCATEGORY_ID))
                .thenReturn(Optional.empty());

            // When & Then
            assertThrows(RuntimeException.class,
                () -> getSubCategoryService.getSubCategory(CATEGORY_ID, SUBCATEGORY_ID));
            verify(readSubCategoryRepositoryPort, times(1)).findOneById(CATEGORY_ID,
                SUBCATEGORY_ID);
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
                new GetSubCategoryResponseDto(1L,
                    "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE),
                new GetSubCategoryResponseDto(2L,
                    "SubCategoryName", CATEGORY_ID, TEST_DATE, TEST_DATE)
            );
            when(readSubCategoryRepositoryPort.findAllByCategoryId(CATEGORY_ID)).thenReturn(
                expectedList);

            // When
            List<GetSubCategoryResponseDto> actualList = getSubCategoryService.getSubCategories(
                CATEGORY_ID);

            // Then
            assertEquals(expectedList, actualList);
            verify(readSubCategoryRepositoryPort, times(1)).findAllByCategoryId(CATEGORY_ID);
        }
    }
}
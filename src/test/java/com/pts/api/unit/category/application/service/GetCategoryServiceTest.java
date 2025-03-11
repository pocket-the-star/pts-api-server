package com.pts.api.unit.category.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import com.pts.api.category.application.port.out.ReadCategoryRepositoryPort;
import com.pts.api.category.application.service.GetCategoryService;
import com.pts.api.category.common.exception.CategoryNotFoundException;
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
class GetCategoryServiceTest extends BaseUnitTest {

    @Mock
    private ReadCategoryRepositoryPort readCategoryRepositoryPort;

    private GetCategoryService getCategoryService;
    private final Long TEST_ID = 1L;
    private final String TEST_NAME = "CategoryName";
    private final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Given
        getCategoryService = new GetCategoryService(readCategoryRepositoryPort);
    }

    @Nested
    @DisplayName("getCategory 메서드 호출 시")
    class DescribeGetCategory {

        @Test
        @DisplayName("유효한 ID 조회 시 카테고리 DTO 반환")
        void itReturnsCategory() {
            // Given
            GetCategoryResponseDto expected = new GetCategoryResponseDto(TEST_ID, TEST_NAME,
                TEST_DATE, TEST_DATE);
            when(readCategoryRepositoryPort.findOneById(TEST_ID)).thenReturn(Optional.of(expected));

            // When
            GetCategoryResponseDto actual = getCategoryService.getCategory(TEST_ID);

            // Then
            assertEquals(expected, actual);
            verify(readCategoryRepositoryPort, times(1)).findOneById(TEST_ID);
        }

        @Test
        @DisplayName("존재하지 않는 ID 조회 시 예외 발생")
        void itThrowsCategoryNotFoundException() {
            // Given
            when(readCategoryRepositoryPort.findOneById(TEST_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CategoryNotFoundException.class,
                () -> getCategoryService.getCategory(TEST_ID));
            verify(readCategoryRepositoryPort, times(1)).findOneById(TEST_ID);
        }
    }

    @Nested
    @DisplayName("getCategories 메서드 호출 시")
    class DescribeGetCategories {

        @Test
        @DisplayName("전체 카테고리 리스트 반환")
        void itReturnsCategoriesList() {
            // Given
            List<GetCategoryResponseDto> expectedList = List.of(
                new GetCategoryResponseDto(TEST_ID, TEST_NAME, TEST_DATE, TEST_DATE),
                new GetCategoryResponseDto(TEST_ID + 1, TEST_NAME + "1", TEST_DATE, TEST_DATE)
            );
            when(readCategoryRepositoryPort.findAll()).thenReturn(expectedList);
            // When
            List<GetCategoryResponseDto> actualList = getCategoryService.getCategories();
            // Then
            assertEquals(expectedList, actualList);
            verify(readCategoryRepositoryPort, times(1)).findAll();
        }
    }
}
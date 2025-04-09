package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("ReadProductLikeApplicationService 클래스")
class ReadProductLikeApplicationServiceTest extends BaseUnitTest {

    private ReadProductLikeApplicationService readProductLikeApplicationService;

    @Mock
    private ProductLikeRepositoryPort productLikeRepository;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        readProductLikeApplicationService = new ReadProductLikeApplicationService(
            productLikeRepository
        );
    }

    @Nested
    @DisplayName("isLiked 메서드 호출 시")
    class DescribeIsLiked {

        @Test
        @DisplayName("좋아요 여부를 반환한다")
        void returnsLikeStatus() {
            // Given
            when(productLikeRepository.existsByProductIdAndUserId(TEST_PRODUCT_ID, TEST_USER_ID))
                .thenReturn(true);

            // When
            boolean result = readProductLikeApplicationService.isLiked(TEST_PRODUCT_ID,
                TEST_USER_ID);

            // Then
            assertThat(result).isTrue();
        }
    }
}
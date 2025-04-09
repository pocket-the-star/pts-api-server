package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("ProductLikeCountService 클래스")
class ReadProductLikeCountServiceTest extends BaseUnitTest {

    private ReadProductLikeCountService productLikeCountService;

    @Mock
    private ProductLikeCountRepositoryPort productLikeCountRepository;

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_COUNT = 10L;

    @BeforeEach
    void setUp() {
        productLikeCountService = new ReadProductLikeCountService(
            productLikeCountRepository);
    }

    @Nested
    @DisplayName("getCount 메서드 호출 시")
    class DescribeGetCount {

        @Nested
        @DisplayName("존재하는 상품이면")
        class WithExistingProduct {

            @BeforeEach
            void setUp() {
                ProductLikeCount productLikeCount = ProductLikeCount.builder()
                    .productId(TEST_PRODUCT_ID)
                    .count(TEST_COUNT)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(productLikeCount));
            }

            @Test
            @DisplayName("좋아요 수를 반환한다")
            void returnsCount() {
                // When
                ProductLikeCount result = productLikeCountService.getCount(TEST_PRODUCT_ID);

                // Then
                assertThat(result.getCount()).isEqualTo(TEST_COUNT);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품이면")
        class WithNonExistingProduct {

            @BeforeEach
            void setUp() {
                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(() -> productLikeCountService.getCount(TEST_PRODUCT_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("좋아요 카운트가 존재하지 않습니다.: " + TEST_PRODUCT_ID);
            }
        }
    }
} 
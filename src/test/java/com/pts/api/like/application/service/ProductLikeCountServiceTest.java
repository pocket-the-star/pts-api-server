package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.domain.model.ProductLikeCount;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;

@DisplayName("ProductLikeCountService 클래스")
class ProductLikeCountServiceTest {

    private ProductLikeCountService productLikeCountService;
    private ProductLikeCountRepositoryPort productLikeCountRepository;
    private LockRepository productLikeLockRepository;
    private RLock mockLock;

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_COUNT = 10L;

    @BeforeEach
    void setUp() {
        productLikeCountRepository = mock(ProductLikeCountRepositoryPort.class);
        productLikeLockRepository = mock(LockRepository.class);
        mockLock = mock(RLock.class);

        when(productLikeLockRepository.getFairLock(any())).thenReturn(mockLock);

        productLikeCountService = new ProductLikeCountService(productLikeCountRepository,
            productLikeLockRepository);
    }

    @Nested
    @DisplayName("getCount 메서드 호출 시")
    class DescribeGetCount {

        @Nested
        @DisplayName("존재하는 상품이면")
        class WithExistingProduct {

            private ProductLikeCount productLikeCount;

            @BeforeEach
            void setUp() {
                productLikeCount = ProductLikeCount.builder()
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

    @Nested
    @DisplayName("increase 메서드 호출 시")
    class DescribeIncrease {

        @Nested
        @DisplayName("존재하는 상품이면")
        class WithExistingProduct {

            private ProductLikeCount productLikeCount;

            @BeforeEach
            void setUp() {
                productLikeCount = ProductLikeCount.builder()
                    .productId(TEST_PRODUCT_ID)
                    .count(TEST_COUNT)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(productLikeCount));
                when(productLikeCountRepository.save(any(ProductLikeCount.class)))
                    .thenReturn(productLikeCount);
            }

            @Test
            @DisplayName("좋아요 수를 증가시킨다")
            void increasesCount() {
                // When
                productLikeCountService.increase(TEST_PRODUCT_ID);

                // Then
                verify(productLikeCountRepository).save(any(ProductLikeCount.class));
                assertThat(productLikeCount.getCount()).isEqualTo(TEST_COUNT + 1);
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
            @DisplayName("새로운 좋아요 카운트를 생성한다")
            void createsNewCount() {
                // When
                productLikeCountService.increase(TEST_PRODUCT_ID);

                // Then
                verify(productLikeCountRepository).save(any(ProductLikeCount.class));
            }
        }
    }

    @Nested
    @DisplayName("decrease 메서드 호출 시")
    class DescribeDecrease {

        @Nested
        @DisplayName("존재하는 상품이면")
        class WithExistingProduct {

            private ProductLikeCount productLikeCount;

            @BeforeEach
            void setUp() {
                productLikeCount = ProductLikeCount.builder()
                    .productId(TEST_PRODUCT_ID)
                    .count(TEST_COUNT)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(productLikeCount));
                when(productLikeCountRepository.save(any(ProductLikeCount.class)))
                    .thenReturn(productLikeCount);
            }

            @Test
            @DisplayName("좋아요 수를 감소시킨다")
            void decreasesCount() {
                // When
                productLikeCountService.decrease(TEST_PRODUCT_ID);

                // Then
                verify(productLikeCountRepository).save(any(ProductLikeCount.class));
                assertThat(productLikeCount.getCount()).isEqualTo(TEST_COUNT - 1);
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
                assertThatThrownBy(() -> productLikeCountService.decrease(TEST_PRODUCT_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("좋아요 카운트가 존재하지 않습니다.: " + TEST_PRODUCT_ID);

            }
        }
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Test
        @DisplayName("좋아요 카운트를 생성한다")
        void createsCount() {
            // When
            productLikeCountService.create(TEST_PRODUCT_ID);

            // Then
            verify(productLikeCountRepository).save(any(ProductLikeCount.class));
        }
    }
} 
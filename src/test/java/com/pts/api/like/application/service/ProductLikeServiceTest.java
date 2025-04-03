package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProductLikeService 클래스")
class ProductLikeServiceTest {

    private ProductLikeService productLikeService;
    private ProductLikeRepositoryPort productLikeRepository;
    private ProductRepositoryPort productRepository;
    private EventPublisherPort outboxPublisher;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        productLikeRepository = mock(ProductLikeRepositoryPort.class);
        productRepository = mock(ProductRepositoryPort.class);
        outboxPublisher = mock(EventPublisherPort.class);

        productLikeService = new ProductLikeService(
            productLikeRepository,
            productRepository,
            outboxPublisher
        );
    }

    @Nested
    @DisplayName("like 메서드 호출 시")
    class DescribeLike {

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            @BeforeEach
            void setUp() {
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(Product.builder().id(TEST_PRODUCT_ID).build()));
                when(productLikeRepository.findByProductIdAndUserId(TEST_PRODUCT_ID, TEST_USER_ID))
                    .thenReturn(Optional.empty());
                when(productLikeRepository.save(any(ProductLike.class)))
                    .thenReturn(ProductLike.builder()
                        .id(1L)
                        .productId(TEST_PRODUCT_ID)
                        .userId(TEST_USER_ID)
                        .build());
                doNothing().when(outboxPublisher).publish(any(), any());
            }

            @Test
            @DisplayName("좋아요를 생성하고 이벤트를 발행한다")
            void createsLikeAndPublishesEvent() {
                // When
                productLikeService.like(TEST_PRODUCT_ID, TEST_USER_ID);

                // Then
                verify(productLikeRepository).save(any(ProductLike.class));
                verify(outboxPublisher).publish(EventType.PRODUCT_LIKE, new ProductLikeData(TEST_PRODUCT_ID));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품 ID가 주어지면")
        class WithNonExistingProductId {

            @BeforeEach
            void setUp() {
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(() -> productLikeService.like(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 상품입니다.: " + TEST_PRODUCT_ID);

                verify(productLikeRepository, never()).save(any(ProductLike.class));
                verify(outboxPublisher, never()).publish(any(), any());
            }
        }

        @Nested
        @DisplayName("이미 좋아요한 상품이면")
        class WithAlreadyLikedProduct {

            @BeforeEach
            void setUp() {
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(Product.builder().id(TEST_PRODUCT_ID).build()));
                when(productLikeRepository.findByProductIdAndUserId(TEST_PRODUCT_ID, TEST_USER_ID))
                    .thenReturn(Optional.of(ProductLike.builder()
                        .id(1L)
                        .productId(TEST_PRODUCT_ID)
                        .userId(TEST_USER_ID)
                        .build()));
            }

            @Test
            @DisplayName("AlreadyExistException을 발생시킨다")
            void throwsAlreadyExistException() {
                // When & Then
                assertThatThrownBy(() -> productLikeService.like(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(AlreadyExistException.class)
                    .hasMessage("이미 좋아요한 상품입니다.: " + TEST_PRODUCT_ID);

                verify(productLikeRepository, never()).save(any(ProductLike.class));
                verify(outboxPublisher, never()).publish(any(), any());
            }
        }
    }

    @Nested
    @DisplayName("unlike 메서드 호출 시")
    class DescribeUnlike {

        @Nested
        @DisplayName("유효한 요청이 주어지면")
        class WithValidRequest {

            private ProductLike productLike;

            @BeforeEach
            void setUp() {
                productLike = ProductLike.builder()
                    .id(1L)
                    .productId(TEST_PRODUCT_ID)
                    .userId(TEST_USER_ID)
                    .build();

                when(productLikeRepository.findByProductIdAndUserId(TEST_PRODUCT_ID, TEST_USER_ID))
                    .thenReturn(Optional.of(productLike));
                doNothing().when(productLikeRepository).delete(productLike);
                doNothing().when(outboxPublisher).publish(any(), any());
            }

            @Test
            @DisplayName("좋아요를 삭제하고 이벤트를 발행한다")
            void deletesLikeAndPublishesEvent() {
                // When
                productLikeService.unlike(TEST_PRODUCT_ID, TEST_USER_ID);

                // Then
                verify(productLikeRepository).delete(productLike);
                verify(outboxPublisher).publish(EventType.PRODUCT_UNLIKE, new ProductUnLikeData(TEST_PRODUCT_ID));
            }
        }

        @Nested
        @DisplayName("좋아요하지 않은 상품이면")
        class WithNotLikedProduct {

            @BeforeEach
            void setUp() {
                when(productLikeRepository.findByProductIdAndUserId(TEST_PRODUCT_ID, TEST_USER_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(() -> productLikeService.unlike(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("좋아요하지 않은 상품입니다.: " + TEST_PRODUCT_ID);

                verify(productLikeRepository, never()).delete(any(ProductLike.class));
                verify(outboxPublisher, never()).publish(any(), any());
            }
        }
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
            boolean result = productLikeService.isLiked(TEST_PRODUCT_ID, TEST_USER_ID);

            // Then
            assertThat(result).isTrue();
        }
    }
} 
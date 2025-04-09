package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.common.exception.AlreadyExistException;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import com.pts.api.product.application.port.out.ProductRepositoryPort;
import com.pts.api.product.domain.model.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("PostProductLikeApplicationService 클래스")
class PostProductLikeApplicationServiceTest extends BaseUnitTest {

    private PostProductLikeApplicationService productLikeApplicationService;

    @Mock
    private ProductLikeRepositoryPort productLikeRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private EventPublisherPort outboxPublisher;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        productLikeApplicationService = new PostProductLikeApplicationService(
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
                productLikeApplicationService.like(TEST_PRODUCT_ID, TEST_USER_ID);

                // Then
                verify(productLikeRepository).save(any(ProductLike.class));
                verify(outboxPublisher).publish(EventType.PRODUCT_LIKE,
                    new ProductLikeData(TEST_PRODUCT_ID));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 상품이면")
        class WithNonExistingProduct {

            @BeforeEach
            void setUp() {
                when(productRepository.findById(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(
                    () -> productLikeApplicationService.like(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 상품입니다.: " + TEST_PRODUCT_ID);
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
                assertThatThrownBy(
                    () -> productLikeApplicationService.like(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(AlreadyExistException.class)
                    .hasMessage("이미 좋아요한 상품입니다.: " + TEST_PRODUCT_ID);
            }
        }
    }
}
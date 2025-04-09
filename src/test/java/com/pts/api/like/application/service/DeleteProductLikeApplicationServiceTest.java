package com.pts.api.like.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.EventPublisherPort;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.out.ProductLikeRepositoryPort;
import com.pts.api.like.domain.model.ProductLike;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("DeleteProductLikeApplicationService 클래스")
class DeleteProductLikeApplicationServiceTest extends BaseUnitTest {

    private DeleteProductLikeApplicationService deleteProductLikeApplicationService;

    @Mock
    private ProductLikeRepositoryPort productLikeRepository;

    @Mock
    private EventPublisherPort outboxPublisher;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        deleteProductLikeApplicationService = new DeleteProductLikeApplicationService(
            productLikeRepository,
            outboxPublisher
        );
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
                deleteProductLikeApplicationService.unlike(TEST_PRODUCT_ID, TEST_USER_ID);

                // Then
                verify(productLikeRepository).delete(productLike);
                verify(outboxPublisher).publish(EventType.PRODUCT_UNLIKE,
                    new ProductUnLikeData(TEST_PRODUCT_ID));
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
                assertThatThrownBy(
                    () -> deleteProductLikeApplicationService.unlike(TEST_PRODUCT_ID, TEST_USER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("좋아요하지 않은 상품입니다.: " + TEST_PRODUCT_ID);
            }
        }
    }
}
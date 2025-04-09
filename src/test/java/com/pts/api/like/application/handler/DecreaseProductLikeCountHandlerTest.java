package com.pts.api.like.application.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductUnLikeData;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.application.service.handler.DecreaseProductLikeCountHandler;
import com.pts.api.like.domain.model.ProductLikeCount;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RLock;

@DisplayName("DecreaseProductLikeCountHandler 클래스")
public class DecreaseProductLikeCountHandlerTest extends BaseUnitTest {

    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_COUNT = 10L;
    private DecreaseProductLikeCountHandler decreaseProductLikeCountHandler;
    @Mock
    private ProductLikeCountRepositoryPort productLikeCountRepository;
    @Mock
    private LockRepository productLikeLockRepository;
    @Mock
    private RLock mockLock;

    @BeforeEach
    void setUp() {
        decreaseProductLikeCountHandler = new DecreaseProductLikeCountHandler(
            productLikeCountRepository, productLikeLockRepository);
    }

    @Nested
    @DisplayName("handle 메서드 호출 시")
    class DescribeDecrease {

        private final Event<ProductUnLikeData> event = Event.<ProductUnLikeData>builder()
            .type(EventType.PRODUCT_UNLIKE)
            .payload(new ProductUnLikeData(TEST_PRODUCT_ID))
            .build();


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

                when(productLikeLockRepository.getFairLock(any())).thenReturn(mockLock);
                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.of(productLikeCount));
                when(productLikeCountRepository.save(any(ProductLikeCount.class)))
                    .thenReturn(productLikeCount);
            }

            @Test
            @DisplayName("좋아요 수를 감소시킨다")
            void decreasesCount() {
                // When
                decreaseProductLikeCountHandler.handle(event);

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
                when(productLikeLockRepository.getFairLock(any())).thenReturn(mockLock);
                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(() -> decreaseProductLikeCountHandler.handle(event))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("좋아요 카운트가 존재하지 않습니다.: " + TEST_PRODUCT_ID);
            }
        }
    }
}

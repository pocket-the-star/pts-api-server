package com.pts.api.like.application.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductLikeData;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.application.service.handler.IncreaseProductLikeCountHandler;
import com.pts.api.like.domain.model.ProductLikeCount;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RLock;

@DisplayName("IncreaseProductLikeCountHandler 클래스")
public class IncreaseProductLikeCountHandlerTest extends BaseUnitTest {

    @Mock
    private ProductLikeCountRepositoryPort productLikeCountRepository;

    @Mock
    private LockRepository productLikeLockRepository;

    @Mock
    private RLock mockLock;

    private IncreaseProductLikeCountHandler increaseProductLikeCountHandler;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_COUNT = 10L;

    @BeforeEach
    void setUp() {
        increaseProductLikeCountHandler = new IncreaseProductLikeCountHandler(
            productLikeCountRepository, productLikeLockRepository);
    }

    @Nested
    @DisplayName("handle 메서드 호출 시")
    class DescribeIncrease {

        private final Event<ProductLikeData> event = Event.<ProductLikeData>builder()
            .type(EventType.PRODUCT_LIKE)
            .payload(new ProductLikeData(TEST_PRODUCT_ID))
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
            @DisplayName("좋아요 수를 증가시킨다")
            void increasesCount() {
                // When
                increaseProductLikeCountHandler.handle(event);

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
                when(productLikeLockRepository.getFairLock(any())).thenReturn(mockLock);
                when(productLikeCountRepository.findByProductId(TEST_PRODUCT_ID))
                    .thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("새로운 좋아요 카운트를 생성한다")
            void createsNewCount() {
                // When
                increaseProductLikeCountHandler.handle(event);

                // Then
                verify(productLikeCountRepository).save(any(ProductLikeCount.class));
            }
        }
    }
}

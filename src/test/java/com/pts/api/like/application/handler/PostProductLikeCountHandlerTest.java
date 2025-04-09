package com.pts.api.like.application.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.lock.repository.LockRepository;
import com.pts.api.lib.internal.shared.event.Event;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.ProductCreateData;
import com.pts.api.like.application.port.out.ProductLikeCountRepositoryPort;
import com.pts.api.like.application.service.handler.PostProductLikeCountHandler;
import com.pts.api.like.domain.model.ProductLikeCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RLock;

@DisplayName("PostProductLikeCountHandler 클래스")
public class PostProductLikeCountHandlerTest {

    @Mock
    private ProductLikeCountRepositoryPort productLikeCountRepository;

    @Mock
    private LockRepository productLikeLockRepository;

    @Mock
    private RLock mockLock;

    private PostProductLikeCountHandler postProductLikeCountHandler;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_COUNT = 10L;

    @BeforeEach
    void setUp() {
        postProductLikeCountHandler = new PostProductLikeCountHandler(
            productLikeCountRepository,
            productLikeLockRepository
        );
    }

    @Nested
    @DisplayName("handle 메서드 호출 시")
    class DescribeCreate extends BaseUnitTest {

        private final Event<ProductCreateData> event = Event.<ProductCreateData>builder()
            .type(EventType.PRODUCT_CREATE)
            .payload(new ProductCreateData(TEST_PRODUCT_ID))
            .build();

        @Test
        @DisplayName("좋아요 카운트를 생성한다")
        void createsCount() {
            // When
            postProductLikeCountHandler.handle(event);

            // Then
            verify(productLikeCountRepository).save(any(ProductLikeCount.class));
        }
    }
}

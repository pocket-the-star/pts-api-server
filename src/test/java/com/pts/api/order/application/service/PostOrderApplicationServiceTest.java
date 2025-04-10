package com.pts.api.order.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.CreateShippingRequest;
import com.pts.api.order.application.port.out.OrderRepositoryPort;
import com.pts.api.order.application.port.out.ShippingRepositoryPort;
import com.pts.api.order.domain.model.Order;
import com.pts.api.order.domain.model.Shipping;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("OrderApplicationService 클래스")
class PostOrderApplicationServiceTest extends BaseUnitTest {

    private PostOrderApplicationService postOrderApplicationService;

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private ShippingRepositoryPort shippingRepository;

    @Mock
    private FeedRepositoryPort feedRepository;

    @Mock
    private DateTimeUtil dateTimeUtil;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;
    private static final Long TEST_ORDER_ID = 1L;
    private static final Integer TEST_QUANTITY = 1;
    private static final Integer TEST_PRICE = 10000;
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        postOrderApplicationService = new PostOrderApplicationService(
            orderRepository,
            shippingRepository,
            feedRepository,
            dateTimeUtil
        );
    }

    @Nested
    @DisplayName("create 메서드 호출 시")
    class DescribeCreate {

        @Nested
        @DisplayName("존재하는 피드이면")
        class WithExistingFeed {

            private CreateOrderRequest request;

            @BeforeEach
            void setUp() {
                Feed feed = Feed.builder()
                    .id(TEST_FEED_ID)
                    .userId(TEST_USER_ID)
                    .price(TEST_PRICE)
                    .build();

                Shipping shipping = Shipping.builder()
                    .recipientName("Test Receiver")
                    .address("Test Address")
                    .detailAddress("Test Detail Address")
                    .postalCode("12345")
                    .shippingMethod("일반배송")
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                Order order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(TEST_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .shipping(shipping)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                request = new CreateOrderRequest(
                    TEST_FEED_ID,
                    TEST_QUANTITY,
                    new CreateShippingRequest(
                        "Test Receiver",
                        "Test Address",
                        "Test Detail Address",
                        "12345",
                        "일반배송"
                    )
                );

                when(feedRepository.findOneById(TEST_FEED_ID)).thenReturn(Optional.of(feed));
                when(dateTimeUtil.now()).thenReturn(TEST_DATE_TIME);
                when(orderRepository.save(any(Order.class))).thenReturn(order);
                when(shippingRepository.save(any(Shipping.class))).thenReturn(shipping);
            }

            @Test
            @DisplayName("주문을 생성한다")
            void createsOrder() {
                // When
                postOrderApplicationService.create(TEST_USER_ID, request);

                // Then
                verify(orderRepository).save(any(Order.class));
                verify(shippingRepository).save(any(Shipping.class));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 피드이면")
        class WithNonExistingFeed {

            private CreateOrderRequest request;

            @BeforeEach
            void setUp() {
                request = new CreateOrderRequest(
                    TEST_FEED_ID,
                    TEST_QUANTITY,
                    new CreateShippingRequest(
                        "Test Receiver",
                        "Test Address",
                        "Test Detail Address",
                        "12345",
                        "일반배송"
                    )
                );

                when(feedRepository.findOneById(TEST_FEED_ID)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(() -> postOrderApplicationService.create(TEST_USER_ID, request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 피드입니다. feedId=" + TEST_FEED_ID);
            }
        }
    }
} 
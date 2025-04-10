package com.pts.api.order.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.CreateShippingRequest;
import com.pts.api.order.application.dto.request.UpdateOrderRequest;
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

            private Feed feed;
            private Order order;
            private Shipping shipping;
            private CreateOrderRequest request;

            @BeforeEach
            void setUp() {
                feed = Feed.builder()
                    .id(TEST_FEED_ID)
                    .userId(TEST_USER_ID)
                    .price(TEST_PRICE)
                    .build();

                shipping = Shipping.builder()
                    .recipientName("Test Receiver")
                    .address("Test Address")
                    .detailAddress("Test Detail Address")
                    .postalCode("12345")
                    .shippingMethod("일반배송")
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                order = Order.builder()
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

    @Nested
    @DisplayName("updateStatus 메서드 호출 시")
    class DescribeUpdateStatus {

        @Nested
        @DisplayName("존재하는 주문이면")
        class WithExistingOrder {

            private Order order;
            private UpdateOrderRequest request;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(TEST_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                request = new UpdateOrderRequest(OrderStatus.PROCESSING);

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
                when(dateTimeUtil.now()).thenReturn(TEST_DATE_TIME);
                when(orderRepository.save(any(Order.class))).thenReturn(order);
            }

            @Test
            @DisplayName("주문 상태를 업데이트한다")
            void updatesOrderStatus() {
                // When
                postOrderApplicationService.updateStatus(TEST_USER_ID, TEST_ORDER_ID, request);

                // Then
                verify(orderRepository).save(any(Order.class));
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 주문이면")
        class WithNonExistingOrder {

            private UpdateOrderRequest request;

            @BeforeEach
            void setUp() {
                request = new UpdateOrderRequest(OrderStatus.PROCESSING);
                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.updateStatus(TEST_USER_ID, TEST_ORDER_ID,
                        request))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("주문을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("주문한 사용자가 아니면")
        class WithDifferentUser {

            private Order order;
            private UpdateOrderRequest request;
            private static final Long DIFFERENT_USER_ID = 2L;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(DIFFERENT_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                request = new UpdateOrderRequest(OrderStatus.PROCESSING);

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("UnAuthorizedException을 발생시킨다")
            void throwsUnAuthorizedException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.updateStatus(TEST_USER_ID, TEST_ORDER_ID,
                        request))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("해당 주문에 대한 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("cancel 메서드 호출 시")
    class DescribeCancel {

        @Nested
        @DisplayName("존재하는 주문이면")
        class WithExistingOrder {

            private Order order;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(TEST_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
                when(dateTimeUtil.now()).thenReturn(TEST_DATE_TIME);
                when(orderRepository.save(any(Order.class))).thenReturn(order);
            }

            @Test
            @DisplayName("주문을 취소한다")
            void cancelsOrder() {
                // When
                postOrderApplicationService.cancel(TEST_USER_ID, TEST_ORDER_ID);

                // Then
                verify(orderRepository).save(any(Order.class));
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 주문이면")
        class WithNonExistingOrder {

            @BeforeEach
            void setUp() {
                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.cancel(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("주문을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("주문한 사용자가 아니면")
        class WithDifferentUser {

            private Order order;
            private static final Long DIFFERENT_USER_ID = 2L;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(DIFFERENT_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("UnAuthorizedException을 발생시킨다")
            void throwsUnAuthorizedException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.cancel(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("해당 주문에 대한 권한이 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드 호출 시")
    class DescribeDelete {

        @Nested
        @DisplayName("존재하는 주문이면")
        class WithExistingOrder {

            private Order order;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(TEST_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
                when(dateTimeUtil.now()).thenReturn(TEST_DATE_TIME);
                when(orderRepository.save(any(Order.class))).thenReturn(order);
            }

            @Test
            @DisplayName("주문을 삭제한다")
            void deletesOrder() {
                // When
                postOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID);

                // Then
                verify(orderRepository).save(any(Order.class));
                assertThat(order.getDeletedAt()).isNotNull();
            }
        }

        @Nested
        @DisplayName("존재하지 않는 주문이면")
        class WithNonExistingOrder {

            @BeforeEach
            void setUp() {
                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.empty());
            }

            @Test
            @DisplayName("NotFoundException을 발생시킨다")
            void throwsNotFoundException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("주문을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("주문한 사용자가 아니면")
        class WithDifferentUser {

            private Order order;
            private static final Long DIFFERENT_USER_ID = 2L;

            @BeforeEach
            void setUp() {
                order = Order.builder()
                    .id(TEST_ORDER_ID)
                    .userId(DIFFERENT_USER_ID)
                    .feedId(TEST_FEED_ID)
                    .quantity(TEST_QUANTITY)
                    .orderStatus(OrderStatus.PENDING)
                    .createdAt(TEST_DATE_TIME)
                    .updatedAt(TEST_DATE_TIME)
                    .build();

                when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
            }

            @Test
            @DisplayName("UnAuthorizedException을 발생시킨다")
            void throwsUnAuthorizedException() {
                // When & Then
                assertThatThrownBy(
                    () -> postOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("해당 주문에 대한 권한이 없습니다.");
            }
        }
    }
} 
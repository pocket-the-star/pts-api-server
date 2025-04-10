package com.pts.api.order.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.port.out.OrderRepositoryPort;
import com.pts.api.order.domain.model.Order;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


@DisplayName("DeleteOrderApplicationService 클래스")
class DeleteOrderApplicationServiceTest extends BaseUnitTest {

    private DeleteOrderApplicationService deleteOrderApplicationService;

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private DateTimeUtil dateTimeUtil;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FEED_ID = 1L;
    private static final Long TEST_ORDER_ID = 1L;
    private static final Integer TEST_QUANTITY = 1;
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        deleteOrderApplicationService = new DeleteOrderApplicationService(
            orderRepository,
            dateTimeUtil
        );
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
                deleteOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID);

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
                    () -> deleteOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("주문을 찾을 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("주문한 사용자가 아니면")
        class WithDifferentUser {

            private static final Long DIFFERENT_USER_ID = 2L;

            @BeforeEach
            void setUp() {
                Order order = Order.builder()
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
                    () -> deleteOrderApplicationService.delete(TEST_USER_ID, TEST_ORDER_ID))
                    .isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("해당 주문에 대한 권한이 없습니다.");
            }
        }
    }
}
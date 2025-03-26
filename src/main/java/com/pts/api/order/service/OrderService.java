package com.pts.api.order.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.outbox.publisher.OutboxPublisher;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.event.EventType;
import com.pts.api.lib.internal.shared.event.data.OrderCancelData;
import com.pts.api.lib.internal.shared.event.data.OrderCreateData;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.dto.request.CreateOrderRequestDto;
import com.pts.api.order.model.Order;
import com.pts.api.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxPublisher outboxPublisher;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public Order create(Long userId, Integer price, CreateOrderRequestDto dto) {
        Order order = Order.builder()
            .buyerId(userId)
            .feedId(dto.feedId())
            .quantity(dto.quantity())
            .price(price)
            .status(OrderStatus.PENDING)
            .createdAt(dateTimeUtil.now())
            .updatedAt(dateTimeUtil.now())
            .build();

        Order savedOrder = orderRepository.save(order);

        outboxPublisher.publish(
            EventType.ORDER_CREATE,
            new OrderCreateData(savedOrder.getFeedId(), savedOrder.getQuantity())
        );

        return savedOrder;
    }

    @Transactional
    public void cancel(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));

        if (!order.getBuyerId().equals(userId)) {
            throw new IllegalStateException("주문을 취소할 권한이 없습니다. orderId=" + orderId);
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("취소할 수 없는 주문입니다. orderId=" + orderId);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(dateTimeUtil.now());
        orderRepository.save(order);

        outboxPublisher.publish(
            EventType.ORDER_CANCEL,
            new OrderCancelData(order.getFeedId(), order.getQuantity())
        );
    }

    @Transactional
    public void updateStatus(Long userId, Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));

        if (!order.getBuyerId().equals(userId)) {
            throw new NotFoundException("해당 주문에 대한 권한이 없습니다. orderId=" + orderId);
        }

        order.setStatus(status);
        order.setUpdatedAt(dateTimeUtil.now());
        orderRepository.save(order);
    }

    @Transactional
    public void delete(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));

        if (!order.getBuyerId().equals(userId)) {
            throw new NotFoundException("해당 주문에 대한 권한이 없습니다. orderId=" + orderId);
        }

        order.setDeletedAt(dateTimeUtil.now());
        orderRepository.save(order);
    }
}

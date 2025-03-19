package com.pts.api.order.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.dto.request.CreateOrderRequestDto;
import com.pts.api.order.model.Order;
import com.pts.api.order.repository.OrderRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DateTimeUtil dateTimeUtil;

    public Order create(Long userId, Integer price, CreateOrderRequestDto createOrderRequestDto) {
        Order order = createOrderRequestDto.toOrder();
        order.setBuyerId(userId);
        order.setPrice(price);
        order.setStatus(OrderStatus.PENDING);
        LocalDateTime now = dateTimeUtil.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        return orderRepository.save(order);
    }

    public Order updateStatus(Long userId, Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));

        if (!order.getBuyerId().equals(userId)) {
            throw new UnauthorizedException(
                "해당 주문에 대한 권한이 없습니다. orderId=" + orderId + ", userId=" + userId);
        }
        order.setStatus(status);
        order.setUpdatedAt(dateTimeUtil.now());

        return orderRepository.save(order);
    }

    public Order cancel(Long userId, Long orderId) {
        return updateStatus(userId, orderId, OrderStatus.CANCELLED);
    }

    public void delete(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 주문입니다. orderId=" + orderId));

        if (!order.getBuyerId().equals(userId)) {
            throw new UnauthorizedException(
                "해당 주문에 대한 권한이 없습니다. orderId=" + orderId + ", userId=" + userId);
        }
        orderRepository.save(order);
    }


}

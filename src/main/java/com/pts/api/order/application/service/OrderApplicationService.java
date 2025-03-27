package com.pts.api.order.application.service;

import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.dto.request.UpdateOrderRequest;
import com.pts.api.order.application.port.in.CreateOrderUseCase;
import com.pts.api.order.application.port.in.UpdateOrderUseCase;
import com.pts.api.order.application.port.out.OrderRepositoryPort;
import com.pts.api.order.application.port.out.ShippingRepositoryPort;
import com.pts.api.order.domain.model.Order;
import com.pts.api.order.domain.model.Shipping;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderApplicationService implements CreateOrderUseCase, UpdateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final ShippingRepositoryPort shippingRepository;
    private final FeedRepositoryPort feedRepository;
    private final DateTimeUtil dateTimeUtil;

    @Override
    @Transactional
    public void create(Long userId, CreateOrderRequest request) {
        feedRepository.findOneById(request.feedId())
            .orElseThrow(() -> new NotFoundException(
                "존재하지 않는 피드입니다. feedId=" + request.feedId()));

        LocalDateTime now = dateTimeUtil.now();

        Shipping shipping = Shipping.builder()
            .recipientName(request.shipping().recipientName())
            .address(request.shipping().address())
            .detailAddress(request.shipping().detailAddress())
            .postalCode(request.shipping().postalCode())
            .shippingMethod(request.shipping().shippingMethod())
            .createdAt(now)
            .updatedAt(now)
            .build();

        Order order = Order.builder()
            .userId(userId)
            .feedId(request.feedId())
            .quantity(request.quantity())
            .orderStatus(OrderStatus.PENDING)
            .shipping(shipping)
            .createdAt(now)
            .updatedAt(now)
            .build();

        order = orderRepository.save(order);
        shipping.setOrderId(order.getId());
        shippingRepository.save(shipping);
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException("해당 주문에 대한 권한이 없습니다.");
        }

        order.updateStatus(request.orderStatus(), dateTimeUtil.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancel(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException("해당 주문에 대한 권한이 없습니다.");
        }

        order.cancel(dateTimeUtil.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new UnauthorizedException("해당 주문에 대한 권한이 없습니다.");
        }

        order.delete(dateTimeUtil.now());
        orderRepository.save(order);
    }
}
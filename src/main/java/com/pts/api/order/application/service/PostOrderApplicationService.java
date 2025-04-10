package com.pts.api.order.application.service;

import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.dto.request.CreateOrderRequest;
import com.pts.api.order.application.port.in.PostOrderUseCase;
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
public class PostOrderApplicationService implements PostOrderUseCase {

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
}
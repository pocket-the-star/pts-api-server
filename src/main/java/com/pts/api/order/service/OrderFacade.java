package com.pts.api.order.service;

import com.pts.api.feed.application.port.out.FeedRepositoryPort;
import com.pts.api.feed.domain.model.Feed;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.order.dto.request.CreateOrderRequestDto;
import com.pts.api.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ShippingService shippingService;
    private final FeedRepositoryPort feedRepository;

    @Transactional
    public void create(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        Feed feed = feedRepository.findOneById(createOrderRequestDto.feedId())
            .orElseThrow(() -> new NotFoundException(
                "존재하지 않는 피드입니다. feedId=" + createOrderRequestDto.feedId()));

        Order order = orderService.create(userId, feed.getPrice(), createOrderRequestDto);
        shippingService.create(order.getId(), createOrderRequestDto.shipping());
    }
}

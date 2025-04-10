package com.pts.api.order.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.global.common.exception.UnAuthorizedException;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.application.port.in.DeleteOrderUseCase;
import com.pts.api.order.application.port.out.OrderRepositoryPort;
import com.pts.api.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteOrderApplicationService implements DeleteOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final DateTimeUtil dateTimeUtil;

    /**
     * 주문 삭제
     *
     * @param userId  사용자 ID
     * @param orderId 주문 ID
     */
    @Override
    @Transactional
    public void delete(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new UnAuthorizedException("해당 주문에 대한 권한이 없습니다.");
        }

        order.delete(dateTimeUtil.now());
        orderRepository.save(order);
    }
}

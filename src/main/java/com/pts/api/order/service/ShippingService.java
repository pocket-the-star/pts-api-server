package com.pts.api.order.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.lib.internal.shared.util.date.DateTimeUtil;
import com.pts.api.order.dto.request.CreateShippingRequestDto;
import com.pts.api.order.model.Shipping;
import com.pts.api.order.repository.ShippingRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;
    private final DateTimeUtil dateTimeUtil;

    public Shipping create(Long orderId,
        CreateShippingRequestDto createShippingRequestDto) {
        LocalDateTime now = dateTimeUtil.now();
        Shipping shipping = createShippingRequestDto.toShipping();
        shipping.setOrderId(orderId);
        shipping.setCreatedAt(now);
        shipping.setUpdatedAt(now);

        return shippingRepository.save(shipping);
    }

    public Shipping updateShippingStatus(Long shippingId, String shippingStatus) {
        Shipping shipping = shippingRepository.findById(shippingId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 배송입니다. shippingId=" + shippingId));

        shipping.setShippingStatus(shippingStatus);
        shipping.setUpdatedAt(dateTimeUtil.now());

        return shippingRepository.save(shipping);
    }

    public void delete(Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 배송입니다. shippingId=" + shippingId));
        shipping.setDeletedAt(dateTimeUtil.now());

        shippingRepository.save(shipping);
    }
}

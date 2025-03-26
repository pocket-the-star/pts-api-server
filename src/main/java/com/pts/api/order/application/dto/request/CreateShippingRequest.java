package com.pts.api.order.application.dto.request;

import com.pts.api.order.domain.model.Shipping;
import lombok.NonNull;

public record CreateShippingRequest(
    @NonNull
    String recipientName,
    @NonNull
    String address,
    @NonNull
    String detailAddress,
    @NonNull
    String postalCode,
    @NonNull
    String shippingMethod
) {

    public Shipping toShipping() {
        return Shipping.builder()
            .recipientName(recipientName)
            .address(address)
            .detailAddress(detailAddress)
            .postalCode(postalCode)
            .shippingMethod(shippingMethod)
            .build();
    }
} 
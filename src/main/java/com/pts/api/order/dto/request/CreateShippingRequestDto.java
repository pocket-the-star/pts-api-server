package com.pts.api.order.dto.request;

import com.pts.api.order.model.Shipping;
import lombok.NonNull;

public record CreateShippingRequestDto(
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

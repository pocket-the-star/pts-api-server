package com.pts.api.order.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;

public class Shipping extends BaseModel {
    private Long id;
    private Long orderId;
    private Long buyerId;
    private String address;
    private String detailAddress;
    private String postalCode;
    private String shippingMethod;
    private String trackingNumber;
    private String shippingStatus;
    private LocalDateTime shippedDate;
    private LocalDateTime expectedDate;

    public Shipping(Long id, Long orderId, Long buyerId, String address, String detailAddress,
                           String postalCode, String shippingMethod, String trackingNumber,
                           String shippingStatus, LocalDateTime shippedDate, LocalDateTime expectedDate,
                           LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.address = address;
        this.detailAddress = detailAddress;
        this.postalCode = postalCode;
        this.shippingMethod = shippingMethod;
        this.trackingNumber = trackingNumber;
        this.shippingStatus = shippingStatus;
        this.shippedDate = shippedDate;
        this.expectedDate = expectedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
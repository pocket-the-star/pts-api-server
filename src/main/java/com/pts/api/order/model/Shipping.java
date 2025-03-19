package com.pts.api.order.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipping")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shipping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_status", nullable = false)
    private String shippingStatus;

    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;

    @Column(name = "expected_date")
    private LocalDateTime expectedDate;

    @Builder
    public Shipping(Long id, Long orderId, String recipientName, String address,
        String detailAddress, String postalCode,
        String shippingMethod, String trackingNumber, String shippingStatus,
        LocalDateTime shippedDate, LocalDateTime expectedDate,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.orderId = orderId;
        this.recipientName = recipientName;
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
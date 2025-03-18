package com.pts.api.lib.internal.shared.enums;

public enum ShippingStatus {
    PENDING,      // 배송 준비 중
    SHIPPED,      // 발송 완료 (배송 중)
    DELIVERED,    // 배송 완료
    RETURNED,     // 반품 처리 완료
    CANCELED      // 배송 취소
}

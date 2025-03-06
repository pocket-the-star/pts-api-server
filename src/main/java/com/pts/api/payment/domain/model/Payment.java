package com.pts.api.payment.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment extends BaseModel {
    private Long paymentId;
    private Long orderId;
    private Long buyerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime paymentDate;

    public Payment(Long paymentId, Long orderId, Long buyerId, BigDecimal amount,
                   String paymentMethod, String paymentStatus, String transactionId,
                   LocalDateTime paymentDate, LocalDateTime createdAt, LocalDateTime updatedAt,
                   LocalDateTime deletedAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
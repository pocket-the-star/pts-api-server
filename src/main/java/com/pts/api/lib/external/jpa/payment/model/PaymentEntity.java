package com.pts.api.lib.external.jpa.payment.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.external.jpa.order.model.MarketOrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MarketOrderEntity order;

    @Column(name = "buyer_id")
    private Long buyerId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Builder
    public PaymentEntity(Long paymentId, MarketOrderEntity order, Long buyerId, BigDecimal amount,
        String paymentMethod, String paymentStatus, String transactionId,
        LocalDateTime paymentDate,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.paymentId = paymentId;
        this.order = order;
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
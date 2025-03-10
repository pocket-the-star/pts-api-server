package com.pts.api.lib.external.jpa.order.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.enums.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "market_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketOrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_option_id")
    private Long productOptionId;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "immediate_flag", nullable = false)
    private boolean immediateFlag;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public MarketOrderEntity(Long id, Long userId, Long productOptionId, OrderType orderType,
        BigDecimal price, int quantity, boolean immediateFlag, LocalDateTime orderDate,
        OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.productOptionId = productOptionId;
        this.orderType = orderType;
        this.price = price;
        this.quantity = quantity;
        this.immediateFlag = immediateFlag;
        this.orderDate = orderDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
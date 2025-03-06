package com.pts.api.order.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.lib.internal.shared.enums.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MarketOrder extends BaseModel {
    private Long id;
    private Long userId;
    private Long productOptionId;
    private OrderType orderType;
    private BigDecimal price;
    private int quantity;
    private boolean immediateFlag;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public MarketOrder(Long id, Long userId, Long productOptionId, OrderType orderType,
                       BigDecimal price, int quantity, boolean immediateFlag, LocalDateTime orderDate,
                       OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
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
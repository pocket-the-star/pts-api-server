package com.pts.api.order.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompletedOrder extends BaseModel {
    private Long id;
    private Long orderId;
    private Long buyerId;
    private Long sellerId;
    private Long productOptionId;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime orderDate;

    public CompletedOrder(Long id, Long orderId, Long buyerId, Long sellerId, Long productOptionId,
                          BigDecimal price, int quantity, LocalDateTime orderDate,
                          LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productOptionId = productOptionId;
        this.price = price;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
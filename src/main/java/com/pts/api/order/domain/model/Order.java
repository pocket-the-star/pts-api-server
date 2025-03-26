package com.pts.api.order.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends BaseModel {

    private Long id;
    private Long userId;
    private Long feedId;
    private int quantity;
    private Long price;
    private OrderStatus orderStatus;
    private Shipping shipping;

    @Builder
    public Order(Long id, Long userId, Long feedId, int quantity, Long price,
        OrderStatus orderStatus,
        Shipping shipping,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.feedId = feedId;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
        this.shipping = shipping;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public void updateStatus(OrderStatus orderStatus, LocalDateTime now) {
        this.orderStatus = orderStatus;
        markUpdated(now);
    }

    public void cancel(LocalDateTime now) {
        orderStatus = OrderStatus.CANCELLED;
        markUpdated(now);
    }

    public void delete(LocalDateTime now) {
        markDeleted(now);
    }
} 
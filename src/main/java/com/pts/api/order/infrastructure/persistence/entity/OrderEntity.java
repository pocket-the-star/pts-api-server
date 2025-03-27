package com.pts.api.order.infrastructure.persistence.entity;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
import com.pts.api.order.domain.model.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feed_id", nullable = false)
    private Long feedId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public OrderEntity(Long id, Long feedId, Long buyerId, Long price,
        Integer quantity, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.feedId = feedId;
        this.buyerId = buyerId;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Order toDomain() {
        return Order.builder()
            .id(id)
            .feedId(feedId)
            .userId(buyerId)
            .price(price)
            .quantity(quantity)
            .orderStatus(status)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static OrderEntity fromModel(Order order) {
        return OrderEntity.builder()
            .id(order.getId())
            .feedId(order.getFeedId())
            .buyerId(order.getUserId())
            .price(order.getPrice())
            .quantity(order.getQuantity())
            .status(order.getOrderStatus())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .deletedAt(order.getDeletedAt())
            .build();
    }

    public Order toModel() {
        return Order.builder()
            .id(id)
            .feedId(feedId)
            .userId(buyerId)
            .price(price)
            .quantity(quantity)
            .orderStatus(status)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
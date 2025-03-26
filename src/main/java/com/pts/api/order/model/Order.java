package com.pts.api.order.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.OrderStatus;
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
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feed_id", nullable = false)
    private Long feedId;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    public Order(Long id, Long feedId, Long buyerId, Integer price,
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
} 
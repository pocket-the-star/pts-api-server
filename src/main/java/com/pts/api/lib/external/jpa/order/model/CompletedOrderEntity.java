package com.pts.api.lib.external.jpa.order.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "completed_orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompletedOrderEntity extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MarketOrderEntity order;
    
    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;
    
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;
    
    @Column(name = "product_option_id", nullable = false)
    private Long productOptionId;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @Column(name = "quantity", nullable = false)
    private int quantity;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Builder
    public CompletedOrderEntity(Long id, MarketOrderEntity order, Long buyerId, Long sellerId, Long productOptionId,
                                BigDecimal price, int quantity, LocalDateTime orderDate,
                                OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        this.id = id;
        this.order = order;
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
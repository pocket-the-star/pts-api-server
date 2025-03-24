package com.pts.api.like.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_like_counts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLikeCount {

    @Id
    private Long productId;
    private Long count;

    @Builder
    public ProductLikeCount(Long productId, Long count) {
        this.productId = productId;
        this.count = count;
    }

    public void increment() {
        this.count++;
    }

    public void decrement() {
        this.count--;
    }
}

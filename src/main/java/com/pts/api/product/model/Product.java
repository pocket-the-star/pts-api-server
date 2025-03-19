package com.pts.api.product.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_category_id", nullable = false)
    private Long subCategoryId;

    @Column(name = "artist_id", nullable = false)
    private Long artistId;

    @Column(name = "max_sell_price", nullable = false)
    private Integer maxSellPrice;

    @Column(name = "min_buy_price", nullable = false)
    private Integer minBuyPrice;

    @OneToMany(mappedBy = "product")
    private List<ProductOption> options;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> images;

    @Builder
    public Product(Long id, String title, Long subCategoryId, Long artistId, Integer maxSellPrice,
        Integer minBuyPrice,
        List<ProductOption> options, List<ProductImage> images, LocalDateTime createdAt,
        LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.subCategoryId = subCategoryId;
        this.artistId = artistId;
        this.maxSellPrice = maxSellPrice;
        this.minBuyPrice = minBuyPrice;
        this.options = options;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getProductThumbnail() {
        return images.get(0).getImageUrl();
    }

    public void priceUpdate(Integer price) {
        this.maxSellPrice = Math.max(this.maxSellPrice, price);
        this.minBuyPrice = Math.min(this.minBuyPrice, price);
    }
} 
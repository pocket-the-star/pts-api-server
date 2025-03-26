package com.pts.api.product.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseModel {

    private Long id;
    private String title;
    private Long subCategoryId;
    private Long artistId;
    private Integer maxSellPrice;
    private Integer minBuyPrice;
    private List<ProductImage> images;

    @Builder
    public Product(Long id, String title, Long subCategoryId, Long artistId,
        Integer maxSellPrice, Integer minBuyPrice, List<ProductImage> images,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.subCategoryId = subCategoryId;
        this.artistId = artistId;
        this.maxSellPrice = maxSellPrice;
        this.minBuyPrice = minBuyPrice;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getProductThumbnail() {
        if (images.isEmpty()) {
            return null;
        }
        return images.get(0).getImageUrl();
    }

    public void priceUpdate(Integer price, LocalDateTime updatedAt) {
        this.maxSellPrice = Math.max(this.maxSellPrice, price);
        this.minBuyPrice = Math.min(this.minBuyPrice, price);
        markUpdated(updatedAt);
    }

    public void delete(LocalDateTime deletedAt) {
        markDeleted(deletedAt);
    }

    public void update(String title, Long subCategoryId, Long artistId, List<ProductImage> images,
        LocalDateTime updatedAt) {
        this.title = title;
        this.subCategoryId = subCategoryId;
        this.artistId = artistId;
        this.images = images;
        markUpdated(updatedAt);
    }
} 
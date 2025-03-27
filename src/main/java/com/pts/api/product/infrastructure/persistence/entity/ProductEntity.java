package com.pts.api.product.infrastructure.persistence.entity;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.product.domain.model.Product;
import com.pts.api.product.domain.model.ProductImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long subCategoryId;
    private Long artistId;
    private Integer maxSellPrice;
    private Integer minBuyPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImageEntity> images;

    @Builder
    public ProductEntity(Long id, String title, Long subCategoryId, Long artistId,
        Integer maxSellPrice, Integer minBuyPrice, List<ProductImageEntity> images,
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

    public static ProductEntity fromModel(Product product) {
        List<ProductImageEntity> imageEntities = product.getImages().stream()
            .map(ProductImageEntity::fromModel)
            .collect(Collectors.toList());

        ProductEntity entity = ProductEntity.builder()
            .id(product.getId())
            .title(product.getTitle())
            .subCategoryId(product.getSubCategoryId())
            .artistId(product.getArtistId())
            .maxSellPrice(product.getMaxSellPrice())
            .minBuyPrice(product.getMinBuyPrice())
            .images(imageEntities)
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .deletedAt(product.getDeletedAt())
            .build();

        imageEntities.forEach(image -> image.setProduct(entity));
        return entity;
    }

    public Product toModel() {
        List<ProductImage> domainImages = images.stream()
            .map(ProductImageEntity::toModel)
            .collect(Collectors.toList());

        return Product.builder()
            .id(id)
            .title(title)
            .subCategoryId(subCategoryId)
            .artistId(artistId)
            .maxSellPrice(maxSellPrice)
            .minBuyPrice(minBuyPrice)
            .images(domainImages)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }
} 
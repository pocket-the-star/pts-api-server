package com.pts.api.product.application.dto.request;

import com.pts.api.product.domain.model.Product;
import com.pts.api.product.domain.model.ProductImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public record CreateProductRequest(
    Long id,
    String title,
    Long subCategoryId,
    Long artistId,
    List<String> images
) {

    public Product toProduct(LocalDateTime now) {
        List<ProductImage> productImages = IntStream.range(0, images.size())
            .mapToObj(i -> ProductImage.builder()
                .sortOrder(i)
                .imageUrl(images.get(i))
                .createdAt(now)
                .updatedAt(now)
                .build())
            .toList();

        Product newProduct = Product.builder()
            .id(id)
            .title(title)
            .subCategoryId(subCategoryId)
            .artistId(artistId)
            .images(productImages)
            .createdAt(now)
            .updatedAt(now)
            .build();

        productImages.forEach(image -> image.setProduct(newProduct));

        return newProduct;
    }
}

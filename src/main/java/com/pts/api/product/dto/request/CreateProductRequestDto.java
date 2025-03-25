package com.pts.api.product.dto.request;

import com.pts.api.product.model.Product;
import com.pts.api.product.model.ProductImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public record CreateProductRequestDto(
    Long id,
    String title,
    Long subCategoryId,
    Long artistId,
    List<String> images
) {

    public Product toProduct(LocalDateTime now) {
        Product newProduct = Product.builder()
            .id(id)
            .title(title)
            .subCategoryId(subCategoryId)
            .artistId(artistId)
            .images(IntStream.range(0, images.size())
                .mapToObj(i -> ProductImage.builder()
                    .sortOrder(i)
                    .imageUrl(images.get(i))
                    .createdAt(now)
                    .updatedAt(now)
                    .build())
                .toList())
            .createdAt(now)
            .updatedAt(now)
            .build();

        newProduct.getImages().forEach(image -> image.setProduct(newProduct));

        return newProduct;
    }
}

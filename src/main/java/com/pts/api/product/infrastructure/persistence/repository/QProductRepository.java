package com.pts.api.product.infrastructure.persistence.repository;


import static com.pts.api.category.infrastructure.persistence.entity.QCategoryEntity.categoryEntity;
import static com.pts.api.category.infrastructure.persistence.entity.QSubCategoryEntity.subCategoryEntity;
import static com.pts.api.product.infrastructure.persistence.entity.QProductEntity.productEntity;

import com.pts.api.product.domain.model.Product;
import com.pts.api.product.infrastructure.persistence.entity.ProductEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QProductRepository {

    private final JPAQueryFactory queryFactory;

    public List<Product> findAll(Long artistId, Long categoryId, Long subCategoryId,
        Long offset, int limit) {
        JPAQuery<ProductEntity> query = queryFactory.select(productEntity)
            .from(productEntity);

        BooleanBuilder builder = new BooleanBuilder();
        if (artistId != null) {
            builder.and(productEntity.artistId.eq(artistId));
            query.innerJoin(categoryEntity).on(categoryEntity.id.eq(productEntity.subCategoryId));
        }

        if (subCategoryId != null) {
            builder.and(productEntity.subCategoryId.eq(subCategoryId));
            query.innerJoin(subCategoryEntity).on(subCategoryEntity.id.eq(subCategoryId));
            if (categoryId != null) {
                builder.and(subCategoryEntity.categoryId.eq(categoryId));
            }
        }

        builder.and(productEntity.deletedAt.isNull());

        return query.where(builder).limit(limit)
            .offset(offset).orderBy(productEntity.updatedAt.desc())
            .fetch()
            .stream()
            .map(ProductEntity::toModel)
            .toList();
    }

    public Optional<Product> findOneById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(productEntity)
                .where(productEntity.id.eq(id)
                    .and(productEntity.deletedAt.isNull())
                )
                .fetchOne()
        ).map(ProductEntity::toModel);
    }
}

package com.pts.api.product.infrastructure.persistence.repository;


import static com.pts.api.product.infrastructure.persistence.model.QProductEntity.productEntity;

import com.pts.api.product.domain.model.Product;
import com.pts.api.product.infrastructure.persistence.model.ProductEntity;
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
        int offset) {
        int LIMIT = 20;
        JPAQuery<ProductEntity> query = queryFactory.select(productEntity)
            .from(productEntity);

        BooleanBuilder builder = new BooleanBuilder();
//        if (artistId != null) {
//            builder.and(productEntity.artistId.eq(artistId));
//            query.innerJoin(category).on(category.id.eq(productEntity.subCategoryId));
//        }

//        if (subCategoryId != null) {
//            builder.and(productEntity.subCategoryId.eq(subCategoryId));
//            query.innerJoin(subCategory).on(subCategory.id.eq(subCategoryId));
//            if (categoryId != null) {
//                builder.and(subCategory.categoryId.eq(categoryId));
//            }
//        }

        builder.and(productEntity.deletedAt.isNull());

        return query.where(builder).limit(LIMIT)
            .offset(offset).orderBy(productEntity.updatedAt.desc())
            .fetch()
            .stream()
            .map(ProductEntity::toDomain)
            .toList();
    }

    public Optional<Product> findOneById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(productEntity)
                .where(productEntity.id.eq(id)
//                    .and(productEntity.deletedAt().isNull())
                )
                .fetchOne()
        ).map(ProductEntity::toDomain);
    }
}

package com.pts.api.product.infrastructure.persistence.adapter;

import static com.pts.api.lib.external.jpa.category.model.QCategoryEntity.categoryEntity;
import static com.pts.api.lib.external.jpa.category.model.QSubCategoryEntity.subCategoryEntity;
import static com.pts.api.lib.external.jpa.product.model.QProductEntity.productEntity;
import static com.pts.api.lib.external.jpa.product.model.QProductImageEntity.productImageEntity;

import com.pts.api.lib.external.jpa.product.model.QProductImageEntity;
import com.pts.api.product.application.dto.response.GetProductResponseDto;
import com.pts.api.product.application.port.out.ReadProductRepositoryPort;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadProductRepository implements ReadProductRepositoryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GetProductResponseDto> findAll(Long groupId, Long categoryId,
        Long subCategoryId,
        int offset) {
        int LIMIT = 20;
        int IMAGE_LIMIT = 1;
        JPAQuery
            <GetProductResponseDto> query = queryFactory
            .select(Projections.constructor(GetProductResponseDto.class,
                productEntity.id,
                productEntity.title,
                productImageEntity.imageUrl,
                productEntity.groupId,
                productEntity.categoryId,
                subCategoryEntity.id,
                productEntity.minBuyPrice,
                productEntity.maxSellPrice,
                productEntity.createdAt,
                productEntity.updatedAt
            ))
            .from(productEntity);

        BooleanBuilder builder = new BooleanBuilder();
        if (groupId != null) {
            builder.and(productEntity.groupId.eq(groupId));
            query.innerJoin(categoryEntity).on(categoryEntity.id.eq(productEntity.categoryId));
        }

        if (subCategoryId != null) {
            builder.and(productEntity.categoryId.eq(subCategoryId));
            builder.and(subCategoryEntity.categoryId.eq(categoryId));
            query.innerJoin(subCategoryEntity)
                .on(subCategoryEntity.id.eq(productEntity.categoryId));

        }

        builder.and(productEntity.deletedAt.isNull());

        QProductImageEntity productImageEntitySub = new QProductImageEntity(
            "productImageEntitySub");
        JPQLQuery<Long> firstProductImageQuery = JPAExpressions
            .select(productImageEntitySub.id)
            .from(productImageEntitySub)
            .where(productImageEntitySub.product.eq(productEntity))
            .orderBy(productImageEntitySub.id.desc())
            .limit(IMAGE_LIMIT);

        return query
            .innerJoin(productImageEntity)
            .on(productImageEntity.id.eq(firstProductImageQuery))
            .where(builder)
            .limit(LIMIT)
            .offset(offset)
            .orderBy(productEntity.updatedAt.desc())
            .fetch();
    }
}

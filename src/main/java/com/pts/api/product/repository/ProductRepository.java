package com.pts.api.product.repository;

import static com.pts.api.category.model.QCategory.category;
import static com.pts.api.category.model.QSubCategory.subCategory;
import static com.pts.api.product.model.QProduct.product;

import com.pts.api.product.model.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final JPAQueryFactory queryFactory;

    public List<Product> findAll(Long groupId, Long categoryId, Long subCategoryId,
        int offset) {
        int LIMIT = 20;
        JPAQuery<Product> query = queryFactory.select(product)
            .from(product);

        BooleanBuilder builder = new BooleanBuilder();
        if (groupId != null) {
            builder.and(product.groupId.eq(groupId));
            query.innerJoin(category).on(category.id.eq(product.categoryId));
        }

        if (subCategoryId != null) {
            builder.and(product.categoryId.eq(subCategoryId));
            query.innerJoin(subCategory).on(subCategory.id.eq(subCategoryId));
            if (categoryId != null) {
                builder.and(subCategory.categoryId.eq(categoryId));
            }
        }

        builder.and(product.deletedAt.isNull());

        return query.where(builder).limit(LIMIT)
            .offset(offset).orderBy(product.updatedAt.desc()).fetch();
    }

    public Optional<Product> findOneById(Long id) {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(product)
                .where(product.id.eq(id).and(product.deletedAt.isNull()))
                .fetchOne()
        );
    }
}

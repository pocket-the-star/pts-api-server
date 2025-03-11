package com.pts.api.lib.external.jpa.category.repository;

import com.pts.api.lib.external.jpa.category.model.SubCategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaSubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    @Query("SELECT s FROM SubCategoryEntity s WHERE s.categoryId = :categoryId")
    List<SubCategoryEntity> findAllByCategoryId(Long categoryId);

    @Query("SELECT s FROM SubCategoryEntity s WHERE s.id = :id AND s.categoryId = :categoryId")
    Optional<SubCategoryEntity> findOneById(Long categoryId, Long id);
}
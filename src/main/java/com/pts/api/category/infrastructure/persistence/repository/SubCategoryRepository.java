package com.pts.api.category.infrastructure.persistence.repository;

import com.pts.api.category.infrastructure.persistence.entity.SubCategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    @Query("SELECT s FROM SubCategoryEntity s WHERE s.categoryId = :categoryId")
    List<SubCategoryEntity> findAllByCategoryId(Long categoryId);

    @Query("SELECT s FROM SubCategoryEntity s WHERE s.id = :id AND s.categoryId = :categoryId")
    Optional<SubCategoryEntity> findOneByIdAndCategoryId(Long categoryId, Long id);
}

package com.pts.api.category.repository;

import com.pts.api.category.model.SubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    @Query("SELECT s FROM SubCategory s WHERE s.categoryId = :categoryId")
    List<SubCategory> findAllByCategoryId(Long categoryId);

    @Query("SELECT s FROM SubCategory s WHERE s.id = :id AND s.categoryId = :categoryId")
    Optional<SubCategory> findOneById(Long categoryId, Long id);
}

package com.pts.api.idol.infrastructure.persistence.repository;

import com.pts.api.idol.infrastructure.persistence.entity.IdolEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdolRepository extends JpaRepository<IdolEntity, Long> {

    @Query(
        value = """
            select *
            from idols
            where idols.deleted_at is null
            order by idols.id desc
            limit :limit offset :offset
            """,
        nativeQuery = true
    )
    List<IdolEntity> findAll(Long offset, Integer limit);

    @Query(
        value = """
            select *
            from idols
            where idols.id = :id
                and idols.deleted_at is null
            limit 1
            """,
        nativeQuery = true
    )
    Optional<IdolEntity> findOneById(Long id);
}

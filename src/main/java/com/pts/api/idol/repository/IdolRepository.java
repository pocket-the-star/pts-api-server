package com.pts.api.idol.repository;

import com.pts.api.idol.model.Idol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdolRepository extends JpaRepository<Idol, Long> {

    @Query(
        value = """
            select *
            from idol
            where deleted_at is null
            limit :limit offset :offset
            order by id desc
            """,
        nativeQuery = true
    )
    List<Idol> findAll(Long offset, Integer limit);

    @Query(
        value = """
            select *
            from idol
            where id = :id
                and deleted_at is null
            limit 1
            """,
        nativeQuery = true
    )
    Optional<Idol> findOneById(Long id);
}

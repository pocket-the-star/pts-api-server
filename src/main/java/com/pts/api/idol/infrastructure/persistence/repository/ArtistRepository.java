package com.pts.api.idol.infrastructure.persistence.repository;

import com.pts.api.idol.infrastructure.persistence.entity.ArtistEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {

    @Query(
        value = """
            select *
            from artists
            where artists.idol_id = :idolId
                and artists.deleted_at is null
            order by artists.id desc
            """,
        nativeQuery = true
    )
    List<ArtistEntity> findAll(Long idolId);

    @Query(
        value = """
            select *
            from artists
            where artists.id = :id
                and artists.deleted_at is null
            limit 1
            """,
        nativeQuery = true
    )
    Optional<ArtistEntity> findOneById(Long id);

}

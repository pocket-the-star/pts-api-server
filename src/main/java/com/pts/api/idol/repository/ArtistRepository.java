package com.pts.api.idol.repository;

import com.pts.api.idol.model.Artist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

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
    List<Artist> findAll(Long idolId);

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
    Optional<Artist> findOneById(Long id);

}

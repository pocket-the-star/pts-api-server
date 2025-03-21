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
            from artist
            inner join idol
                on artist.idol_id = idol.id
            where deleted_at is null
            limit :limit offset :offset
            order by id desc
            """,
        nativeQuery = true
    )
    List<Artist> findAll(Long idolId, Long offset, Integer limit);

    @Query(
        value = """
            select *
            from artist
            where id = :id
                and deleted_at is null
            limit 1
            """,
        nativeQuery = true
    )
    Optional<Artist> findOneById(Long id);

}

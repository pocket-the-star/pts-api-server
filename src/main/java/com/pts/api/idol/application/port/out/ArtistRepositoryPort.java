package com.pts.api.idol.application.port.out;

import com.pts.api.idol.domain.model.Artist;
import java.util.List;
import java.util.Optional;

public interface ArtistRepositoryPort {

    List<Artist> findAll(Long idolId);

    Optional<Artist> findOneById(Long id);
}

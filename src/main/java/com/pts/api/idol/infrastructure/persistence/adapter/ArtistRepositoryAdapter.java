package com.pts.api.idol.infrastructure.persistence.adapter;

import com.pts.api.idol.application.port.out.ArtistRepositoryPort;
import com.pts.api.idol.domain.model.Artist;
import com.pts.api.idol.infrastructure.persistence.entity.ArtistEntity;
import com.pts.api.idol.infrastructure.persistence.repository.ArtistRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArtistRepositoryAdapter implements ArtistRepositoryPort {

    private final ArtistRepository artistRepository;


    @Override
    public List<Artist> findAll(Long idolId) {
        return artistRepository.findAll(idolId).stream().map(ArtistEntity::toModel).toList();
    }

    @Override
    public Optional<Artist> findOneById(Long id) {
        return artistRepository.findOneById(id).map(ArtistEntity::toModel);
    }
}

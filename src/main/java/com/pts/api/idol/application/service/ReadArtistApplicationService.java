package com.pts.api.idol.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadArtistResponse;
import com.pts.api.idol.application.port.in.ReadArtistUseCase;
import com.pts.api.idol.application.port.out.ArtistRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadArtistApplicationService implements ReadArtistUseCase {

    private final ArtistRepositoryPort artistRepository;

    @Override
    public List<ReadArtistResponse> getArtists(Long idolId) {
        return artistRepository.findAll(idolId)
            .stream()
            .map(ReadArtistResponse::of)
            .toList();
    }

    @Override
    public ReadArtistResponse getArtist(Long id) {
        return artistRepository.findOneById(id)
            .map(ReadArtistResponse::of)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 아티스트입니다. id=" + id));
    }

}

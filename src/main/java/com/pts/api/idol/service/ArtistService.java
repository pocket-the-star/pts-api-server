package com.pts.api.idol.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.dto.response.ReadArtistResponseDto;
import com.pts.api.idol.repository.ArtistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public List<ReadArtistResponseDto> findAll(Long idolId, Long offset, Integer limit) {
        return artistRepository.findAll(idolId, offset, limit)
            .stream()
            .map(ReadArtistResponseDto::of)
            .toList();
    }

    public ReadArtistResponseDto findOneById(Long id) {
        return artistRepository.findOneById(id)
            .map(ReadArtistResponseDto::of)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 아티스트입니다. id=" + id));
    }

}

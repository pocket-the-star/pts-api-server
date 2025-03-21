package com.pts.api.idol.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.dto.response.ReadIdolResponseDto;
import com.pts.api.idol.repository.IdolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdolService {

    private final IdolRepository idolRepository;

    public List<ReadIdolResponseDto> findAll(Long offset, Integer limit) {
        return idolRepository.findAll(offset, limit)
            .stream()
            .map(ReadIdolResponseDto::of)
            .toList();
    }

    public ReadIdolResponseDto findOneById(Long id) {
        return idolRepository.findOneById(id)
            .map(ReadIdolResponseDto::of)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 아이돌입니다. id=" + id));
    }
}

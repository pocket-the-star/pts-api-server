package com.pts.api.idol.application.service;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadIdolResponse;
import com.pts.api.idol.application.port.in.ReadIdolUseCase;
import com.pts.api.idol.application.port.out.IdolRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadIdolApplicationService implements ReadIdolUseCase {

    private final IdolRepositoryPort idolRepository;

    @Override
    public List<ReadIdolResponse> getIdols(Long offset, Integer limit) {
        return idolRepository.findAll(offset, limit)
            .stream()
            .map(ReadIdolResponse::of)
            .toList();
    }

    @Override
    public ReadIdolResponse getIdol(Long id) {
        return idolRepository.findOneById(id)
            .map(ReadIdolResponse::of)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 아이돌입니다. id=" + id));
    }
}

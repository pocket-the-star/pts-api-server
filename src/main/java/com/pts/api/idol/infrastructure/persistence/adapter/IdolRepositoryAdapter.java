package com.pts.api.idol.infrastructure.persistence.adapter;

import com.pts.api.idol.application.port.out.IdolRepositoryPort;
import com.pts.api.idol.domain.model.Idol;
import com.pts.api.idol.infrastructure.persistence.entity.IdolEntity;
import com.pts.api.idol.infrastructure.persistence.repository.IdolRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdolRepositoryAdapter implements IdolRepositoryPort {

    private final IdolRepository idolRepository;

    @Override
    public List<Idol> findAll(Long offset, Integer limit) {
        return idolRepository.findAll(offset, limit).stream().map(IdolEntity::toDomain).toList();
    }

    @Override
    public Optional<Idol> findOneById(Long id) {
        return idolRepository.findById(id).map(IdolEntity::toDomain);
    }
}
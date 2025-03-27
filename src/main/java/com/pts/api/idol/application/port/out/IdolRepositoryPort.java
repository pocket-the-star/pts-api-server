package com.pts.api.idol.application.port.out;

import com.pts.api.idol.domain.model.Idol;
import java.util.List;
import java.util.Optional;

public interface IdolRepositoryPort {

    List<Idol> findAll(Long offset, Integer limit);

    Optional<Idol> findOneById(Long id);
} 
package com.pts.api.category.application.port.out;

import com.pts.api.category.application.dto.response.GetCategoryResponseDto;
import java.util.List;
import java.util.Optional;

public interface ReadCategoryRepositoryPort {

    List<GetCategoryResponseDto> findAll();

    Optional<GetCategoryResponseDto> findOneById(Long id);
}

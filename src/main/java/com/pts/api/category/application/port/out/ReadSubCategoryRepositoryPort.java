package com.pts.api.category.application.port.out;

import com.pts.api.category.application.dto.response.GetSubCategoryResponseDto;
import java.util.List;
import java.util.Optional;

public interface ReadSubCategoryRepositoryPort {

    Optional<GetSubCategoryResponseDto> findOneById(Long categoryId, Long id);

    List<GetSubCategoryResponseDto> findAllByCategoryId(Long categoryId);

}

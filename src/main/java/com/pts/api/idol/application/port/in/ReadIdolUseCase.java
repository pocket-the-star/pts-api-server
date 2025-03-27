package com.pts.api.idol.application.port.in;

import com.pts.api.idol.application.dto.response.ReadIdolResponse;
import java.util.List;

public interface ReadIdolUseCase {

    List<ReadIdolResponse> getIdols(Long offset, Integer limit);

    ReadIdolResponse getIdol(Long id);

}

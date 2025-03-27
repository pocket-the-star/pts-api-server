package com.pts.api.idol.application.port.in;

import com.pts.api.idol.application.dto.response.ReadArtistResponse;
import java.util.List;

public interface ReadArtistUseCase {

    List<ReadArtistResponse> getArtists(Long idolId);

    ReadArtistResponse getArtist(Long id);
}

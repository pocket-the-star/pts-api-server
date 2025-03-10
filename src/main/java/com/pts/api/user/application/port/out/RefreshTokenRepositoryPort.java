package com.pts.api.user.application.port.out;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {

    void save(Long userId, String token);

    Optional<String> findOneById(Long userId);

    void deleteById(Long userId);
}

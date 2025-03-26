package com.pts.api.user.application.port.out;

import com.pts.api.user.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
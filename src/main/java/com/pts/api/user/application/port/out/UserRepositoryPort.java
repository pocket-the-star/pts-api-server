package com.pts.api.user.application.port.out;

import com.pts.api.user.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findOneByEmail(String email);

    User save(User user);
}

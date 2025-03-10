package com.pts.api.user.infrastructure.persistence.adapter;

import com.pts.api.lib.external.jpa.user.repository.JpaUserRepository;
import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.infrastructure.mapper.UserMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findOneByEmail(String email) {
        return jpaUserRepository.findOneByEmail(email).map(userMapper::mapToModel);
    }

    @Override
    public User save(User user) {
        return userMapper.mapToModel(jpaUserRepository.save(userMapper.mapToEntity(user)));
    }

}

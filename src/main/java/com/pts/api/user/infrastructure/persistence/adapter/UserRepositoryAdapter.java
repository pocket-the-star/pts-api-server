package com.pts.api.user.infrastructure.persistence.adapter;

import com.pts.api.user.application.port.out.UserRepositoryPort;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.infrastructure.persistence.model.UserEntity;
import com.pts.api.user.infrastructure.persistence.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(UserEntity.from(user)).toDomain();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
            .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findOneByEmail(email)
            .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 
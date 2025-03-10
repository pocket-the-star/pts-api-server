package com.pts.api.lib.external.jpa.user.repository;

import com.pts.api.lib.external.jpa.user.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByEmail(String email);
}

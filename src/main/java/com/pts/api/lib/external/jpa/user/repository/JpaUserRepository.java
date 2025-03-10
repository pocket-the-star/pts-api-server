package com.pts.api.lib.external.jpa.user.repository;

import com.pts.api.lib.external.jpa.user.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.localAccount.email = :email")
    Optional<UserEntity> findOneByEmail(String email);
}

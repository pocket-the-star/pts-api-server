package com.pts.api.user.infrastructure.persistence.repository;

import com.pts.api.user.infrastructure.persistence.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.localAccountEntity la WHERE la.email = :email")
    Optional<UserEntity> findOneByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM UserEntity u JOIN u.localAccountEntity la WHERE la.email = :email")
    boolean existsByEmail(String email);
} 
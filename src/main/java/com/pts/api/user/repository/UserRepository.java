package com.pts.api.user.repository;

import com.pts.api.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.localAccount la WHERE la.email = :email")
    Optional<User> findOneByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u JOIN u.localAccount la WHERE la.email = :email")
    boolean existsByEmail(String email);
} 
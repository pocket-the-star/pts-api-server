package com.pts.api.user.application.port.out;

import com.pts.api.user.domain.model.EmailVerify;
import java.util.Optional;

public interface EmailVerifyRepositoryPort {

    void save(EmailVerify emailVerify);

    Optional<EmailVerify> findByEmail(String email);

    void deleteByEmail(String email);

    boolean getLock(String email);

    void releaseLock(String email);
} 
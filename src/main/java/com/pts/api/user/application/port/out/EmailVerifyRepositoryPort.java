package com.pts.api.user.application.port.out;

import com.pts.api.user.domain.model.EmailVerify;
import java.util.Optional;

public interface EmailVerifyRepositoryPort {

    void save(EmailVerify emailVerify);

    Optional<EmailVerify> findById(String email);

    void deleteById(String email);

    boolean getLock(String email);

    void releaseLock(String email);
}

package com.pts.api.global.outbox.repository;

import com.pts.api.global.outbox.model.Outbox;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Query("SELECT o FROM Outbox o WHERE o.createdAt <= :from")
    List<Outbox> findAllByCreatedAt(
        LocalDateTime from,
        Pageable pageable);

}

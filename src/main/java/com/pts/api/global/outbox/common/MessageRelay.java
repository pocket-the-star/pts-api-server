package com.pts.api.global.outbox.common;

import com.pts.api.global.outbox.model.Outbox;
import com.pts.api.global.outbox.repository.OutboxRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class MessageRelay {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createdOutbox(Outbox outbox) {
        log.info("Outbox Before Commit= {}", outbox);
        outboxRepository.save(outbox);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(Outbox outbox) {
        try {
            kafkaTemplate.send(
                outbox.getEventType().getTopic(),
                outbox.getData()
            ).get(1, TimeUnit.SECONDS);

            outboxRepository.delete(outbox);
        } catch (Exception e) {
            log.error("Outbox After Commit={}", outbox, e);
        }

    }

    private void scheduledPublishEvent(Outbox outbox) {
        try {
            kafkaTemplate.executeInTransaction(operations -> {
                try {
                    operations.send(
                        outbox.getEventType().getTopic(),
                        outbox.getData()
                    ).get(1, TimeUnit.SECONDS);
                    outboxRepository.delete(outbox);
                    return true;
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    log.error("Failed to send message to Kafka: {}", e.getMessage());
                    return false;
                }
            });
        } catch (Exception e) {
            log.error("Outbox After Commit={}", outbox, e);
        }
    }

    @Scheduled(
        fixedDelay = 10,
        initialDelay = 5,
        timeUnit = TimeUnit.SECONDS
    )
    public void publishPendingEvent() {
        log.info("Publish Pending Event Start: {}", LocalDateTime.now());
        List<Outbox> outboxes = outboxRepository.findAllByCreatedAt(
            LocalDateTime.now().minusSeconds(10),
            Pageable.ofSize(100)
        );

        for (Outbox outbox : outboxes) {
            log.info("Publish Pending Event: {}", outbox);

            scheduledPublishEvent(outbox);
        }

        log.info("Publish Pending Event End: {}", LocalDateTime.now());
    }
}

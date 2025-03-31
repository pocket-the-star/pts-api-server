package com.pts.api.global.outbox.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.event.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "outbox")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EventType eventType;
    private String data;

    @Builder
    public Outbox(Long id, EventType eventType, String data, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.id = id;
        this.eventType = eventType;
        this.data = data;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

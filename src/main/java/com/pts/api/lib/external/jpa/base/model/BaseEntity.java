package com.pts.api.lib.external.jpa.base.model;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;

    public BaseEntity() {
    }
} 
package com.pts.api.lib.internal.shared.base.model;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseModel {

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;

    protected void markUpdated(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void markDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}

package com.pts.api.lib.internal.shared.base.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
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

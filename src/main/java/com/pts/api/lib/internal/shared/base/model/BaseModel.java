package com.pts.api.lib.internal.shared.base.model;

import java.time.LocalDateTime;

public abstract class BaseModel {

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;

    BaseModel(LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    protected void markUpdated(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void markDeleted(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}

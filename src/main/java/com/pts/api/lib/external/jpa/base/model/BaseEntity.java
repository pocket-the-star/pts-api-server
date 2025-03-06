package com.pts.api.lib.external.jpa.base.model;

import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    protected OffsetDateTime createdAt;
    protected OffsetDateTime updatedAt;
    protected OffsetDateTime deletedAt;
    
    public BaseEntity() { }
} 
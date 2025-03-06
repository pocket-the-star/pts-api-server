package com.pts.api.idol.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import java.util.List;

public class Idol extends BaseModel {

    private Long id;
    private String name;
    private List<member> members;

    public Idol(Long id, String name, List<member> members,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
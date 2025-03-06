package com.pts.api.idol.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import java.util.List;

public class Group extends BaseModel {
    private Long id;
    private String name;
    private List<Artist> artists;

    public Group(Long id, String name, List<Artist> artists,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
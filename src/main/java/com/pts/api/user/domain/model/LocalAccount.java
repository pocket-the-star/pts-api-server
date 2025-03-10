package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocalAccount extends BaseModel {

    private Long id;
    private Long userId;
    private String email;
    private String password;

    @Builder
    public LocalAccount(Long id, Long userId, String email, String password,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
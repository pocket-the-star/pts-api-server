package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalAccount extends BaseModel {

    private Long id;
    private String email;
    private String password;
    private User user;

    @Builder
    public LocalAccount(Long id, String email, String password, User user,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
} 
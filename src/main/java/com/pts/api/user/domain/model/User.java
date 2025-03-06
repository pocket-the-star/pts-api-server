package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.lib.internal.shared.enums.UserRole;
import java.time.LocalDateTime;

public class User extends BaseModel {

    private Long id;
    private String nickname;
    private UserRole role;
    private LocalAccount localAccount;
    private UserInfo userInfo;

    public User(Long id, String nickname, UserRole role, LocalAccount localAccount,
        UserInfo userInfo,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.localAccount = localAccount;
        this.userInfo = userInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
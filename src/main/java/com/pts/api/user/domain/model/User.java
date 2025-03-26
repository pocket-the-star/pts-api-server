package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import com.pts.api.lib.internal.shared.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseModel {

    private Long id;
    private String nickname;
    private UserRole role;
    private LocalAccount localAccount;
    private UserInfo userInfo;

    @Builder
    public User(Long id, String nickname, UserRole role, LocalAccount localAccount,
        UserInfo userInfo, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.localAccount = localAccount;
        this.userInfo = userInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        if (localAccount != null) {
            this.localAccount.setUser(this);
        }
        if (userInfo != null) {
            this.userInfo.setUser(this);
        }
    }

    public String getEmail() {
        return localAccount.getEmail();
    }

    public void updateNickname(String nickname, LocalDateTime updatedAt) {
        this.nickname = nickname;
        markUpdated(updatedAt);
    }

    public void updateUserInfo(UserInfo userInfo, LocalDateTime updatedAt) {
        this.userInfo = userInfo;
        if (userInfo != null) {
            this.userInfo.setUser(this);
        }
        markUpdated(updatedAt);
    }

    public void delete(LocalDateTime deletedAt) {
        markDeleted(deletedAt);
    }
} 
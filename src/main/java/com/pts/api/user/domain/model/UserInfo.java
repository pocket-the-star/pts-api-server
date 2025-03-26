package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo extends BaseModel {

    private Long id;
    private String name;
    private String phone;
    private String address;
    private String extraInfo;
    private User user;

    @Builder
    public UserInfo(Long id, String name, String phone, String address, String extraInfo, User user,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.extraInfo = extraInfo;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
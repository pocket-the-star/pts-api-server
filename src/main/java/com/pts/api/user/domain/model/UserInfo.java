package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.base.model.BaseModel;
import java.time.LocalDateTime;

public class UserInfo extends BaseModel {

    private Long id;
    private Long userId;
    private String fullName;
    private String phone;
    private String address;
    private String extraInfo;

    public UserInfo(Long id, Long userId, String fullName, String phone, String address,
        String extraInfo,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.extraInfo = extraInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
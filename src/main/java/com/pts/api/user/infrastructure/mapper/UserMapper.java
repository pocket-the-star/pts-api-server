package com.pts.api.user.infrastructure.mapper;

import com.pts.api.lib.external.jpa.user.model.LocalAccountEntity;
import com.pts.api.lib.external.jpa.user.model.UserEntity;
import com.pts.api.lib.external.jpa.user.model.UserInfoEntity;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.domain.model.UserInfo;

public class UserMapper {

    public User mapToModel(UserEntity userEntity) {
        return User.builder()
            .id(userEntity.getId())
            .nickname(userEntity.getNickname())
            .role(userEntity.getRole())
            .localAccount(mapLocalAccountToModel(userEntity.getLocalAccount()))
            .userInfo(mapUserInfoToModel(userEntity.getUserInfo()))
            .build();
    }

    public UserEntity mapToEntity(User user) {
        UserInfoEntity userInfoEntity = null;
        if (user.getUserInfo() != null) {
            userInfoEntity = mapUserInfoToEntity(user.getUserInfo());
        }

        return UserEntity.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .role(user.getRole())
            .localAccount(mapLocalAccountToEntity(user.getLocalAccount()))
            .userInfo(userInfoEntity)
            .build();
    }

    private UserInfo mapUserInfoToModel(UserInfoEntity userInfoEntity) {
        return UserInfo.builder()
            .id(userInfoEntity.getId())
            .userId(userInfoEntity.getUserId())
            .fullName(userInfoEntity.getFullName())
            .phone(userInfoEntity.getPhone())
            .address(userInfoEntity.getAddress())
            .extraInfo(userInfoEntity.getExtraInfo())
            .createdAt(userInfoEntity.getCreatedAt())
            .updatedAt(userInfoEntity.getUpdatedAt())
            .deletedAt(userInfoEntity.getDeletedAt())
            .build();
    }

    private UserInfoEntity mapUserInfoToEntity(UserInfo userInfo) {
        return UserInfoEntity.builder()
            .id(userInfo.getId())
            .userId(userInfo.getUserId())
            .fullName(userInfo.getFullName())
            .phone(userInfo.getPhone())
            .address(userInfo.getAddress())
            .extraInfo(userInfo.getExtraInfo())
            .createdAt(userInfo.getCreatedAt())
            .updatedAt(userInfo.getUpdatedAt())
            .deletedAt(userInfo.getDeletedAt())
            .build();
    }

    private LocalAccountEntity mapLocalAccountToEntity(LocalAccount localAccount) {
        return LocalAccountEntity.builder()
            .id(localAccount.getId())
            .userId(localAccount.getUserId())
            .email(localAccount.getEmail())
            .password(localAccount.getPassword())
            .createdAt(localAccount.getCreatedAt())
            .updatedAt(localAccount.getUpdatedAt())
            .deletedAt(localAccount.getDeletedAt())
            .build();
    }

    private LocalAccount mapLocalAccountToModel(LocalAccountEntity localAccountEntity) {
        return LocalAccount.builder()
            .id(localAccountEntity.getId())
            .userId(localAccountEntity.getUserId())
            .email(localAccountEntity.getEmail())
            .password(localAccountEntity.getPassword())
            .createdAt(localAccountEntity.getCreatedAt())
            .updatedAt(localAccountEntity.getUpdatedAt())
            .deletedAt(localAccountEntity.getDeletedAt())
            .build();
    }
}

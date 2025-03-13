package com.pts.api.user.infrastructure.mapper;

import com.pts.api.lib.external.jpa.user.model.LocalAccountEntity;
import com.pts.api.lib.external.jpa.user.model.UserEntity;
import com.pts.api.lib.external.jpa.user.model.UserInfoEntity;
import com.pts.api.user.domain.model.LocalAccount;
import com.pts.api.user.domain.model.User;
import com.pts.api.user.domain.model.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements IUserMapper {

    @Override
    public User mapToModel(UserEntity userEntity) {
        return User.builder()
            .id(userEntity.getId())
            .nickname(userEntity.getNickname())
            .role(userEntity.getRole())
            .localAccount(mapLocalAccountToModel(userEntity.getLocalAccount()))
            .userInfo(mapUserInfoToModel(userEntity.getUserInfo()))
            .createdAt(userEntity.getCreatedAt())
            .updatedAt(userEntity.getUpdatedAt())
            .deletedAt(userEntity.getDeletedAt())
            .build();
    }

    @Override
    public UserEntity mapToEntity(User user) {
        UserEntity userEntity = UserEntity.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .deletedAt(user.getDeletedAt())
            .build();

        if (user.getLocalAccount() != null) {
            LocalAccountEntity localAccountEntity = LocalAccountEntity.builder()
                .id(user.getLocalAccount().getId())
                .email(user.getLocalAccount().getEmail())
                .password(user.getLocalAccount().getPassword())
                .user(userEntity)
                .createdAt(user.getLocalAccount().getCreatedAt())
                .updatedAt(user.getLocalAccount().getUpdatedAt())
                .deletedAt(user.getLocalAccount().getDeletedAt())
                .build();
            userEntity.setLocalAccount(localAccountEntity);
        }

        if (user.getUserInfo() != null) {
            UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .id(user.getUserInfo().getId())
                .fullName(user.getUserInfo().getFullName())
                .phone(user.getUserInfo().getPhone())
                .address(user.getUserInfo().getAddress())
                .extraInfo(user.getUserInfo().getExtraInfo())
                .user(userEntity)
                .createdAt(user.getUserInfo().getCreatedAt())
                .updatedAt(user.getUserInfo().getUpdatedAt())
                .deletedAt(user.getUserInfo().getDeletedAt())
                .build();
            userEntity.setUserInfo(userInfoEntity);
        }

        return userEntity;
    }

    private UserInfo mapUserInfoToModel(UserInfoEntity userInfoEntity) {
        if (userInfoEntity == null) return null;
        return UserInfo.builder()
            .id(userInfoEntity.getId())
            .userId(userInfoEntity.getUser().getId())
            .fullName(userInfoEntity.getFullName())
            .phone(userInfoEntity.getPhone())
            .address(userInfoEntity.getAddress())
            .extraInfo(userInfoEntity.getExtraInfo())
            .createdAt(userInfoEntity.getCreatedAt())
            .updatedAt(userInfoEntity.getUpdatedAt())
            .deletedAt(userInfoEntity.getDeletedAt())
            .build();
    }

    private LocalAccount mapLocalAccountToModel(LocalAccountEntity localAccountEntity) {
        if (localAccountEntity == null) return null;
        return LocalAccount.builder()
            .id(localAccountEntity.getId())
            .userId(localAccountEntity.getUser().getId())
            .email(localAccountEntity.getEmail())
            .password(localAccountEntity.getPassword())
            .createdAt(localAccountEntity.getCreatedAt())
            .updatedAt(localAccountEntity.getUpdatedAt())
            .deletedAt(localAccountEntity.getDeletedAt())
            .build();
    }
}

package com.pts.api.user.infrastructure.persistence.entity;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.domain.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private LocalAccountEntity localAccountEntity;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserInfoEntity userInfoEntity;

    @Builder
    public UserEntity(Long id, String nickname, UserRole role,
        LocalAccountEntity localAccountEntity,
        UserInfoEntity userInfoEntity, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.nickname = nickname;
        this.role = role;
        this.localAccountEntity = localAccountEntity;
        this.userInfoEntity = userInfoEntity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        if (localAccountEntity != null) {
            this.localAccountEntity.setUserEntity(this);
        }
        if (userInfoEntity != null) {
            this.userInfoEntity.setUserEntity(this);
        }
    }

    public String getEmail() {
        return localAccountEntity.getEmail();
    }

    public User toDomain() {
        return User.builder()
            .id(id)
            .nickname(nickname)
            .role(role)
            .userInfo(userInfoEntity.toDomain())
            .localAccount(localAccountEntity.toDomain())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .role(user.getRole())
            .localAccountEntity(LocalAccountEntity.fromDomain(user.getLocalAccount()))
            .userInfoEntity(UserInfoEntity.from(user.getUserInfo()))
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .deletedAt(user.getDeletedAt())
            .build();
    }
} 
package com.pts.api.user.infrastructure.persistence.entity;

import com.pts.api.user.domain.model.UserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_infos")
@Getter
@Setter
@NoArgsConstructor
public class UserInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String phone;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String extraInfo;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public UserInfoEntity(Long id, String fullName, String phone, String address, String extraInfo,
        UserEntity userEntity, LocalDateTime createdAt, LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.extraInfo = extraInfo;
        this.userEntity = userEntity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public UserInfo toDomain() {
        return UserInfo.builder()
            .name(fullName)
            .phone(phone)
            .address(address)
            .user(userEntity.toDomain())
            .extraInfo(extraInfo)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static UserInfoEntity from(UserInfo userInfo) {
        return UserInfoEntity.builder()
            .fullName(userInfo.getName())
            .phone(userInfo.getPhone())
            .address(userInfo.getAddress())
            .extraInfo(userInfo.getExtraInfo())
            .createdAt(userInfo.getCreatedAt())
            .updatedAt(userInfo.getUpdatedAt())
            .deletedAt(userInfo.getDeletedAt())
            .build();
    }
}
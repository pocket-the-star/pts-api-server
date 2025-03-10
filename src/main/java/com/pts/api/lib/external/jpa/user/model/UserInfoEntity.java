package com.pts.api.lib.external.jpa.user.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String extraInfo;

    @Builder
    public UserInfoEntity(Long id, Long userId, String fullName, String phone, String address,
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
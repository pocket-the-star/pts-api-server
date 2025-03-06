package com.pts.api.lib.external.jpa.user.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.lib.internal.shared.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nickname", nullable = false)
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private LocalAccountEntity localAccount;
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_info_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserInfoEntity userInfo;
    
    @Builder
    public UserEntity(Long id, String nickname, UserRole role, LocalAccountEntity localAccount,
                      UserInfoEntity userInfo, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
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
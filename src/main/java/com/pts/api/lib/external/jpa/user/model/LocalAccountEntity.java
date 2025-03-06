package com.pts.api.lib.external.jpa.user.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "local_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalAccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Builder
    public LocalAccountEntity(Long id, Long userId, String email, String password,
                              OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
} 
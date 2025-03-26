package com.pts.api.user.infrastructure.persistence.model;

import com.pts.api.lib.external.jpa.base.model.BaseEntity;
import com.pts.api.user.domain.model.LocalAccount;
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
@Table(name = "local_accounts")
@Getter
@Setter
@NoArgsConstructor
public class LocalAccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Builder
    public LocalAccountEntity(Long id, String email, String password, UserEntity userEntity,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userEntity = userEntity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public LocalAccount toDomain() {
        return LocalAccount.builder()
            .id(id)
            .email(email)
            .password(password)
            .user(userEntity.toDomain())
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deletedAt(deletedAt)
            .build();
    }

    public static LocalAccountEntity fromDomain(LocalAccount localAccount) {
        return LocalAccountEntity.builder()
            .id(localAccount.getId())
            .email(localAccount.getEmail())
            .password(localAccount.getPassword())
            .userEntity(UserEntity.from(localAccount.getUser()))
            .createdAt(localAccount.getCreatedAt())
            .updatedAt(localAccount.getUpdatedAt())
            .deletedAt(localAccount.getDeletedAt())
            .build();
    }
}
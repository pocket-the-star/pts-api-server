package com.pts.api.user.domain.model;

import com.pts.api.lib.internal.shared.enums.TokenType;
import com.pts.api.lib.internal.shared.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenPayload {

    private Long userId;
    private TokenType tokenType;
    private UserRole userRole;

    @Builder
    public TokenPayload(Long userId, TokenType tokenType, UserRole userRole) {
        this.userId = userId;
        this.tokenType = tokenType;
        this.userRole = userRole;
    }

    public int getTokenTypeValue() {
        return tokenType.getValue();
    }

    public String getUserRoleValue() {
        return userRole.getRole();
    }
}

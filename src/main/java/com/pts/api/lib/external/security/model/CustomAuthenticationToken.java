package com.pts.api.lib.external.security.model;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Long userId;

    public CustomAuthenticationToken(Long userId,
        Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}

package com.pts.api.global.presentation.security.filter;

import com.pts.api.global.presentation.exception.UnauthorizedException;
import com.pts.api.lib.external.security.model.CustomAuthenticationToken;
import com.pts.api.user.application.port.in.ValidateTokenUseCase;
import com.pts.api.user.common.exception.InvalidTokenException;
import com.pts.api.user.domain.model.TokenPayload;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ValidateTokenUseCase validateTokenUseCase;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            try {
                String refreshToken = extractRefreshToken(request.getCookies());
                TokenPayload tokenPayload = validateTokenUseCase.execute(bearerToken, refreshToken);
                List<GrantedAuthority> authorities = buildAuthorities(
                    tokenPayload.getUserRoleValue());

                CustomAuthenticationToken authenticationToken =
                    new CustomAuthenticationToken(tokenPayload.getUserId(), authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (InvalidTokenException e) {
                log.error("Invalid Token", e);
                throw new UnauthorizedException("Invalid Token");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractRefreshToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
            .filter(cookie -> "refreshToken".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
    }

    private List<GrantedAuthority> buildAuthorities(String role) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        return Collections.singletonList(authority);
    }
}

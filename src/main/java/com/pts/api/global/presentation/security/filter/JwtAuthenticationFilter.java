package com.pts.api.global.presentation.security.filter;

import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.lib.external.security.model.CustomAuthenticationToken;
import com.pts.api.user.exception.InvalidTokenException;
import com.pts.api.user.service.TokenService;
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
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            try {
                String refreshToken = extractRefreshToken(request.getCookies());
                tokenService.parse(bearerToken, refreshToken);
                List<GrantedAuthority> authorities = buildAuthorities(
                    tokenService.getUserRole(bearerToken).getRole());

                CustomAuthenticationToken authenticationToken =
                    new CustomAuthenticationToken(tokenService.getUserId(bearerToken), authorities);
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

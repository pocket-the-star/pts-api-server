package com.pts.api.global.presentation.security.filter;

import com.pts.api.global.common.exception.UnauthorizedException;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.lib.external.security.model.CustomAuthenticationToken;
import com.pts.api.lib.internal.shared.util.serializer.DataSerializer;
import com.pts.api.user.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        log.info("request endpoint: {}", request.getRequestURI());
        if (bearerToken != null) {
            try {
                String refreshToken = extractRefreshToken(request.getCookies());
                tokenService.parse(bearerToken, refreshToken);
                List<GrantedAuthority> authorities = buildAuthorities(
                    tokenService.getUserRole(bearerToken).getRole());

                CustomAuthenticationToken authenticationToken =
                    new CustomAuthenticationToken(tokenService.getUserId(bearerToken), authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                log.error("Error Endpoint: {}, Msg: {}", request.getRequestURI(), e.toString());
                handleErrorResponse(response, e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractRefreshToken(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("리프레시 토큰은 필수입니다.");
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

    private void handleErrorResponse(HttpServletResponse response, String errorMessage)
        throws IOException {
        String result = DataSerializer.serialize(
            ResponseGenerator.fail(errorMessage, HttpStatus.UNAUTHORIZED).getBody());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(result);
    }
}

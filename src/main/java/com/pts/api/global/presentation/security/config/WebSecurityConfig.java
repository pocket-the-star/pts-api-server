package com.pts.api.global.presentation.security.config;

import com.pts.api.global.presentation.security.filter.JwtAuthenticationFilter;
import com.pts.api.user.application.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenService tokenService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
            tokenService);

        http.csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                authz -> authz.requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                        "/swagger-ui.html", "/swagger-resources/**", "/api-docs/**",           // 추가
                        "/api-docs/swagger-config", // 추가
                        "/webjars/**").permitAll().requestMatchers("/api/v1/users/auth-code/confirm")
                    .permitAll().requestMatchers("/api/v1/users/sign-in").permitAll()
                    .requestMatchers("/api/v1/users/sign-up").permitAll()
                    .requestMatchers("/api/v1/users/email/verify").permitAll().anyRequest()
                    .authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

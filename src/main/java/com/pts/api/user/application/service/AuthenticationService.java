package com.pts.api.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}

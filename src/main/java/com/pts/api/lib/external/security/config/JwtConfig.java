package com.pts.api.lib.external.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean(name = "jwtSecretKey")
    public String getSecretKey(@Value("${jwt.secret}") String secretKey) {
        return secretKey;
    }

    @Bean(name = "jwtAlgorithm")
    public String getAlgorithm(@Value("${jwt.algorithm}") String algorithm) {
        return algorithm;
    }

}

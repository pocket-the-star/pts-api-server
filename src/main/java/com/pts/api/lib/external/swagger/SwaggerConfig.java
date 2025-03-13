package com.pts.api.lib.external.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .title("PTS API")
            .version("v1.0")
            .description("""                
                 인증 정보
                회원가입과 로그인을 제외한 모든 API 요청에는 `Authorization` 헤더가 필요합니다.
                - 헤더 형식: `Bearer {token}`
                - 예제: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
                - 현재는 scheme = "Bearer"로 인해 Bearer 이 자동 주입됩니다. 토큰값만 넣어주세요
                
                인증 토큰은 로그인 API를 통해 발급받을 수 있습니다.
                
                """)
            .contact(new Contact()
                .name("PTS")
                .email("whdgh9595@gmail.com"));

        // JWT 토큰 설정
        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("bearerAuth");

        // 서버 설정
        Server devServer = new Server()
            .url("http://localhost:8080")
            .description("Development server");

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer))
            .addSecurityItem(securityRequirement)
            .components(new Components()
                .addSecuritySchemes("bearerAuth", bearerAuth));
    }
}

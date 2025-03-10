package com.pts.api.unit.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.pts.api.lib.internal.shared.enums.UserRole;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.out.TokenProviderPort;
import com.pts.api.user.application.service.internal.GenerateTokenService;
import com.pts.api.user.domain.model.TokenPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenerateTokenService 클래스")
class GenerateTokenServiceTest {

    @Mock
    private TokenProviderPort tokenProviderPort;

    private GenerateTokenService generateTokenService;

    @BeforeEach
    void setUp() {
        generateTokenService = new GenerateTokenService(tokenProviderPort);
    }

    @Nested
    @DisplayName("generate 메서드 호출 시")
    class DescribeGenerate {

        @Test
        @DisplayName("정상 토큰 응답을 반환한다")
        void itReturnsTokenResponseDto() {
            // Given
            Long userId = 1L;
            UserRole userRole = UserRole.NORMAL;
            when(tokenProviderPort.create(any(TokenPayload.class), eq(1_800_000L)))
                .thenReturn("accessToken");
            when(tokenProviderPort.create(any(TokenPayload.class), eq(604_800_000L)))
                .thenReturn("refreshToken");

            // When
            TokenResponseDto result = generateTokenService.generate(userId, userRole);

            // Then
            assertThat(result.accessToken()).isEqualTo("accessToken");
            assertThat(result.refreshToken()).isEqualTo("refreshToken");
        }
    }
}

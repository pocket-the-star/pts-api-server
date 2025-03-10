package com.pts.api.integration.user.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.user.application.dto.request.SignInRequestDto;
import com.pts.api.user.application.dto.response.TokenResponseDto;
import com.pts.api.user.application.port.in.SignInUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("SignInController 클래스")
public class SignInControllerTest extends BaseIntegrationTest {

    @Autowired
    private SignInUseCase signInUseCase;

    private static final String SIGN_IN_URL = "/api/v1/users/sign-in";

    @Nested
    @DisplayName("signIn 메서드 호출 시")
    class DescribeSignIn {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 OK 상태코드를 반환하고, 토큰 응답을 포함한다")
            void itReturnsTokenResponse() throws Exception {
                SignInRequestDto requestDto = new SignInRequestDto("test@example.com", "pass123");
                TokenResponseDto tokenResponseDto = new TokenResponseDto("accessToken",
                    "refreshToken");

                when(signInUseCase.execute(any(SignInRequestDto.class))).thenReturn(
                    tokenResponseDto);
                String requestJson = objectMapper.writeValueAsString(requestDto);

                mockMvc.perform(post(SIGN_IN_URL)
                        .with(csrf())
                        .with(user("testUser").roles("NORMAL"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                    .andExpect(status().isOk());
            }
        }
    }
}

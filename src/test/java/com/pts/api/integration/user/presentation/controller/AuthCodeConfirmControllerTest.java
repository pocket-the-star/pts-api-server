package com.pts.api.integration.user.presentation.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.application.port.in.AuthCodeConfirmUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("AuthCodeConfirmController 클래스")
public class AuthCodeConfirmControllerTest extends BaseIntegrationTest {

    @Autowired
    private AuthCodeConfirmUseCase authCodeConfirmUseCase;

    private static final String CONFIRM_URL = "/api/v1/users/auth-code/confirm";

    @Nested
    @DisplayName("verifyEmail 메서드 호출 시")
    class DescribeVerifyEmail {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                String testEmail = "test@example.com";
                String testAuthCode = "abc123";
                AuthCodeConfirmRequestDto request = new AuthCodeConfirmRequestDto(testEmail,
                    testAuthCode);
                String requestJson = new ObjectMapper().writeValueAsString(request);
                doNothing().when(authCodeConfirmUseCase)
                    .confirm(request);

                mockMvc.perform(post(CONFIRM_URL)
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                    .andExpect(status().isOk());
            }
        }
    }
}

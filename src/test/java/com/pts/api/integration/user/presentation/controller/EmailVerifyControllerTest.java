package com.pts.api.integration.user.presentation.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.user.application.dto.request.EmailVerifyRequestDto;
import com.pts.api.user.application.port.in.EmailVerifyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("EmailVerifyController 클래스")
public class EmailVerifyControllerTest extends BaseIntegrationTest {

    @MockitoBean
    private EmailVerifyUseCase emailVerifyUseCase;

    private static final String VERIFY_URL = "/api/v1/users/email/verify";

    @Nested
    @DisplayName("verifyEmail 메서드 호출 시")
    class DescribeVerifyEmail {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 ok 상태코드를 반환한다")
            void itPublishesEmailVerificationEvent() throws Exception {
                // Given
                String testEmail = "test@example.com";
                EmailVerifyRequestDto request = new EmailVerifyRequestDto(testEmail);
                String requestJson = new ObjectMapper().writeValueAsString(request);
                doNothing().when(emailVerifyUseCase).verifyEmail(testEmail);

                // When & Then
                mockMvc.perform(post(VERIFY_URL)
                        .with(user("testUser").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                    .andExpect(status().isOk());
            }
        }
    }
}
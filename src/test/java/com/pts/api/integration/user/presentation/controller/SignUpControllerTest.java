package com.pts.api.integration.user.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.user.application.dto.request.SignUpRequestDto;
import com.pts.api.user.application.port.in.SignUpUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("SignUpController 클래스")
public class SignUpControllerTest extends BaseIntegrationTest {

    @Autowired
    private SignUpUseCase signUpUseCase;

    private static final String SIGN_UP_URL = "/api/v1/users/sign-up";

    @Nested
    @DisplayName("signUp 메서드 호출 시")
    class DescribeSignUp {

        @Nested
        @DisplayName("정상 요청이 주어지면")
        class ContextWithValidRequest {

            @Test
            @DisplayName("200 OK 상태코드를 반환한다")
            void itReturns200Ok() throws Exception {
                String testEmail = "test@example.com";
                String testPassword = "pass123";
                String testPasswordConfirm = "pass123";
                String testNickname = "testUser";
                String testUserRole = "NORMAL";

                SignUpRequestDto request = new SignUpRequestDto(testUserRole, testNickname,
                    testEmail, testPassword,
                    testPasswordConfirm);
                String requestJson = new ObjectMapper().writeValueAsString(request);
                doNothing().when(signUpUseCase).execute(any(SignUpRequestDto.class));

                mockMvc.perform(
                        post(SIGN_UP_URL)
                            .with(user("testUser").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk());
            }
        }
    }
}

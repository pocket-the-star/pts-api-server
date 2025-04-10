package com.pts.api.user.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.SignInRequest;
import com.pts.api.user.application.dto.response.TokenResponse;
import com.pts.api.user.application.port.in.SignInUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("SignInController 클래스")
class SignInControllerTest extends BaseIntegrationTest {

    @Autowired
    private SignInUseCase signInUseCase;

    private static final String BASE_URL = "/api/v1/users";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        reset(signInUseCase);
    }


    @Nested
    @DisplayName("signIn 메서드 호출 시")
    class DescribeSignIn {

        @Test
        @DisplayName("유효한 요청이면 200 OK와 함께 토큰을 반환한다")
        void returns200OkWithTokens() throws Exception {
            // given
            SignInRequest request = new SignInRequest(TEST_EMAIL, TEST_PASSWORD);
            TokenResponse expectedToken = new TokenResponse("access_token", "refresh_token");

            when(signInUseCase.signIn(any(SignInRequest.class))).thenReturn(expectedToken);

            // when & then
            mockMvc.perform(post(BASE_URL + "/sign-in")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value(ResponseMsg.OK.getValue()))
                .andExpect(jsonPath("$.resultCode").value(200))
                .andExpect(jsonPath("$.data.accessToken").value(expectedToken.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(expectedToken.refreshToken()));
        }
    }
}
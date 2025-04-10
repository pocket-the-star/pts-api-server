package com.pts.api.user.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.SignUpRequest;
import com.pts.api.user.application.port.in.SignUpUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("UserController 클래스")
class SignUpControllerTest extends BaseIntegrationTest {

    @Autowired
    private SignUpUseCase signUpUseCase;

    private static final String BASE_URL = "/api/v1/users";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "testUser";

    @BeforeEach
    void setUp() {
        reset(signUpUseCase);
    }

    @Nested
    @DisplayName("signUp 메서드 호출 시")
    class DescribeSignUp {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // given
            SignUpRequest request = new SignUpRequest(
                "USER",
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_PASSWORD
            );

            doNothing().when(signUpUseCase).signUp(any(SignUpRequest.class));

            // when & then
            mockMvc.perform(post(BASE_URL + "/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value(ResponseMsg.OK.getValue()))
                .andExpect(jsonPath("$.resultCode").value(200));
        }
    }
} 
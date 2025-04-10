package com.pts.api.user.presentation.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.EmailVerifyRequest;
import com.pts.api.user.application.port.in.VerifyEmailUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("VerifyEmailController 클래스")
class VerifyEmailControllerTest extends BaseIntegrationTest {

    @Autowired
    private VerifyEmailUseCase verifyEmailUseCase;

    private static final String BASE_URL = "/api/v1/users";
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        reset(verifyEmailUseCase);
    }

    @Nested
    @DisplayName("verifyEmail 메서드 호출 시")
    class DescribeVerifyEmail {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // given
            EmailVerifyRequest request = new EmailVerifyRequest(TEST_EMAIL);

            doNothing().when(verifyEmailUseCase).verifyEmail(TEST_EMAIL);

            // when & then
            mockMvc.perform(post(BASE_URL + "/email/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value(ResponseMsg.OK.getValue()))
                .andExpect(jsonPath("$.resultCode").value(200));
        }
    }
}
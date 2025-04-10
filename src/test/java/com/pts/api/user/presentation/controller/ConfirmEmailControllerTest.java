package com.pts.api.user.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.user.application.dto.request.AuthCodeConfirmRequest;
import com.pts.api.user.application.port.in.ConfirmEmailUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("ConfirmEmailController 클래스")
class ConfirmEmailControllerTest extends BaseIntegrationTest {

    @Autowired
    private ConfirmEmailUseCase confirmEmailUseCase;

    private static final String BASE_URL = "/api/v1/users";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_AUTH_CODE = "123456";

    @BeforeEach
    void setUp() {
        reset(confirmEmailUseCase);
    }

    @Nested
    @DisplayName("confirmAuthCode 메서드 호출 시")
    class DescribeConfirmAuthCode {

        @Test
        @DisplayName("유효한 요청이면 200 OK를 반환한다")
        void returns200Ok() throws Exception {
            // given
            AuthCodeConfirmRequest request = new AuthCodeConfirmRequest(TEST_EMAIL, TEST_AUTH_CODE);

            doNothing().when(confirmEmailUseCase).confirm(any(AuthCodeConfirmRequest.class));

            // when & then
            mockMvc.perform(post(BASE_URL + "/auth-code/confirm")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMsg").value(ResponseMsg.OK.getValue()))
                .andExpect(jsonPath("$.resultCode").value(200));
        }
    }
}
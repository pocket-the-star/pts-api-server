package com.pts.api.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.user.dto.request.AuthCodeConfirmRequestDto;
import com.pts.api.user.dto.request.EmailVerifyRequestDto;
import com.pts.api.user.dto.request.SignInRequestDto;
import com.pts.api.user.dto.request.SignUpRequestDto;
import com.pts.api.user.dto.response.TokenResponseDto;
import com.pts.api.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("UserController 클래스")
public class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    private static final String BASE_URL = "/api/v1/users";
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_NICKNAME = "testUser";

    @Nested
    @DisplayName("회원가입 API [POST /api/v1/users/sign-up]")
    class SignUp {

        private static final String URL = BASE_URL + "/sign-up";

        @Nested
        @DisplayName("유효한 요청이 주어진다면")
        class WithValidRequest {

            private SignUpRequestDto request;

            @BeforeEach
            void setUp() {
                request = new SignUpRequestDto(
                    "USER",
                    TEST_NICKNAME,
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_PASSWORD
                );
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // Given
                doNothing().when(userService).signUp(any(SignUpRequestDto.class));

                // When & Then
                mockMvc.perform(
                        post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("로그인 API [POST /api/v1/users/sign-in]")
    class SignIn {

        private static final String URL = BASE_URL + "/sign-in";

        @Nested
        @DisplayName("유효한 요청이 주어진다면")
        class WithValidRequest {

            private SignInRequestDto request;
            private TokenResponseDto response;

            @BeforeEach
            void setUp() {
                request = new SignInRequestDto(TEST_EMAIL, TEST_PASSWORD);
                response = new TokenResponseDto("accessToken", "refreshToken");
            }

            @Test
            @DisplayName("200 OK와 함께 토큰을 반환한다")
            void returnsTokenResponse() throws Exception {
                // Given
                when(userService.signIn(any(SignInRequestDto.class))).thenReturn(response);

                // When & Then
                mockMvc.perform(
                        post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("이메일 인증 요청 API [POST /api/v1/users/email/verify]")
    class VerifyEmail {

        private static final String URL = BASE_URL + "/email/verify";

        @Nested
        @DisplayName("유효한 요청이 주어진다면")
        class WithValidRequest {

            private EmailVerifyRequestDto request;

            @BeforeEach
            void setUp() {
                request = new EmailVerifyRequestDto(TEST_EMAIL);
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // Given
                doNothing().when(userService).verifyEmail(TEST_EMAIL);

                // When & Then
                mockMvc.perform(
                        post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("인증 코드 확인 API [POST /api/v1/users/auth-code/confirm]")
    class ConfirmAuthCode {

        private static final String URL = BASE_URL + "/auth-code/confirm";

        @Nested
        @DisplayName("유효한 요청이 주어진다면")
        class WithValidRequest {

            private AuthCodeConfirmRequestDto request;

            @BeforeEach
            void setUp() {
                request = new AuthCodeConfirmRequestDto(TEST_EMAIL, "123456");
            }

            @Test
            @DisplayName("200 OK와 함께 성공 응답을 반환한다")
            void returnsOkResponse() throws Exception {
                // Given
                doNothing().when(userService).confirm(any(AuthCodeConfirmRequestDto.class));

                // When & Then
                mockMvc.perform(
                        post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andDo(print());
            }
        }
    }
}

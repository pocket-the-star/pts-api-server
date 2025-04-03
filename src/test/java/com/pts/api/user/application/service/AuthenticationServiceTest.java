package com.pts.api.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.pts.api.common.base.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest extends BaseUnitTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AuthenticationService authenticationService;

    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_WRONG_PASSWORD = "wrongpassword";
    private static final String TEST_ENCODED_PASSWORD = "encoded_password";

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(bCryptPasswordEncoder);
    }

    @Nested
    @DisplayName("matches 메서드 호출 시")
    class DescribeMatches {

        @Nested
        @DisplayName("비밀번호가 일치하면")
        class WhenPasswordMatches {

            @Test
            @DisplayName("true를 반환한다")
            void itReturnsTrue() {
                // given
                when(bCryptPasswordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD))
                    .thenReturn(true);

                // when
                boolean result = authenticationService.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD);

                // then
                assertThat(result).isTrue();
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않으면")
        class WhenPasswordDoesNotMatch {

            @Test
            @DisplayName("false를 반환한다")
            void itReturnsFalse() {
                // given
                when(bCryptPasswordEncoder.matches(TEST_WRONG_PASSWORD, TEST_ENCODED_PASSWORD))
                    .thenReturn(false);

                // when
                boolean result = authenticationService.matches(TEST_WRONG_PASSWORD, TEST_ENCODED_PASSWORD);

                // then
                assertThat(result).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("encode 메서드 호출 시")
    class DescribeEncode {

        @Test
        @DisplayName("비밀번호를 암호화한다")
        void itEncodesPassword() {
            // given
            when(bCryptPasswordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);

            // when
            String result = authenticationService.encode(TEST_PASSWORD);

            // then
            assertThat(result).isEqualTo(TEST_ENCODED_PASSWORD);
        }
    }
} 
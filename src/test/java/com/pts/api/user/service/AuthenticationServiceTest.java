package com.pts.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService 클래스")
public class AuthenticationServiceTest {

    // Data
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationService authenticationService;

    private static final String TEST_RAW_PASSWORD = "testPassword123";
    private static final String TEST_ENCODED_PASSWORD = "$2a$10$abcdefghijklmnopqrstuvwxyz123456";

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(bCryptPasswordEncoder);
    }

    @Nested
    @DisplayName("비밀번호를 검증할 때")
    class Matches {

        @Nested
        @DisplayName("비밀번호가 일치한다면")
        class WithMatchingPasswords {

            @Test
            @DisplayName("true를 반환한다")
            void returnsTrue() {
                // Given
                when(bCryptPasswordEncoder.matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD))
                    .thenReturn(true);

                // When
                boolean result = authenticationService.matches(TEST_RAW_PASSWORD,
                    TEST_ENCODED_PASSWORD);

                // Then
                assertThat(result).isTrue();
                verify(bCryptPasswordEncoder).matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD);
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않는다면")
        class WithNonMatchingPasswords {

            @Test
            @DisplayName("false를 반환한다")
            void returnsFalse() {
                // Given
                when(bCryptPasswordEncoder.matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD))
                    .thenReturn(false);

                // When
                boolean result = authenticationService.matches(TEST_RAW_PASSWORD,
                    TEST_ENCODED_PASSWORD);

                // Then
                assertThat(result).isFalse();
                verify(bCryptPasswordEncoder).matches(TEST_RAW_PASSWORD, TEST_ENCODED_PASSWORD);
            }
        }
    }

    @Nested
    @DisplayName("비밀번호를 암호화할 때")
    class Encode {

        @Test
        @DisplayName("암호화된 비밀번호를 반환한다")
        void returnsEncodedPassword() {
            // Given
            when(bCryptPasswordEncoder.encode(TEST_RAW_PASSWORD))
                .thenReturn(TEST_ENCODED_PASSWORD);

            // When
            String result = authenticationService.encode(TEST_RAW_PASSWORD);

            // Then
            assertThat(result).isEqualTo(TEST_ENCODED_PASSWORD);
            verify(bCryptPasswordEncoder).encode(TEST_RAW_PASSWORD);
        }
    }
}

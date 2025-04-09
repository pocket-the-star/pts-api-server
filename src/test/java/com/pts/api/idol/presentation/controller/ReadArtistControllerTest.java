package com.pts.api.idol.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadArtistResponse;
import com.pts.api.idol.application.port.in.ReadArtistUseCase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("ArtistController 클래스")
class ReadArtistControllerTest extends BaseIntegrationTest {

    @Autowired
    private ReadArtistUseCase readArtistUseCase;

    private static final Long TEST_IDOL_ID = 1L;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 아티스트";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        reset(readArtistUseCase);
    }

    @Nested
    @DisplayName("getList 메서드 호출 시")
    class DescribeGetList {

        @Test
        @DisplayName("모든 아티스트 목록을 반환한다")
        void itReturnsAllArtists() throws Exception {
            // Given
            ReadArtistResponse artist1 = new ReadArtistResponse(TEST_ID, TEST_NAME, TEST_DATE,
                TEST_DATE);
            ReadArtistResponse artist2 = new ReadArtistResponse(2L, "테스트 아티스트 2", TEST_DATE,
                TEST_DATE);
            given(readArtistUseCase.getArtists(TEST_IDOL_ID)).willReturn(List.of(artist1, artist2));

            // When
            ResultActions result = mockMvc.perform(
                get("/api/v1/idols/{idolId}/artists", TEST_IDOL_ID)
                    .with(user("testUser").roles("USER")));

            // Then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getOne 메서드 호출 시")
    class DescribeGetOne {

        @Nested
        @DisplayName("존재하는 아티스트 ID가 주어지면")
        class WithExistingArtistId {

            @Test
            @DisplayName("아티스트 정보를 반환한다")
            void itReturnsArtist() throws Exception {
                // Given
                ReadArtistResponse artist = new ReadArtistResponse(TEST_ID, TEST_NAME, TEST_DATE,
                    TEST_DATE);
                given(readArtistUseCase.getArtist(TEST_ID)).willReturn(artist);

                // When
                ResultActions result = mockMvc.perform(
                    get("/api/v1/idols/{idolId}/artists/{id}", TEST_IDOL_ID, TEST_ID)
                        .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 아티스트 ID가 주어지면")
        class WithNonExistingArtistId {

            @Test
            @DisplayName("404 에러를 반환한다")
            void itReturns404Error() throws Exception {
                // Given
                given(readArtistUseCase.getArtist(TEST_ID))
                    .willThrow(new NotFoundException("존재하지 않는 아티스트입니다. id=" + TEST_ID));

                // When
                ResultActions result = mockMvc.perform(
                    get("/api/v1/idols/{idolId}/artists/{id}", TEST_IDOL_ID, TEST_ID)
                        .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isNotFound());
            }
        }
    }
} 
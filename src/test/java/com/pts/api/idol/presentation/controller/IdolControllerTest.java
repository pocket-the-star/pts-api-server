package com.pts.api.idol.presentation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pts.api.common.base.BaseIntegrationTest;
import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadIdolResponse;
import com.pts.api.idol.application.port.in.ReadIdolUseCase;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("IdolController 클래스")
class IdolControllerTest extends BaseIntegrationTest {

    @Autowired
    private ReadIdolUseCase readIdolUseCase;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 아이돌";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        reset(readIdolUseCase);
    }

    @Nested
    @DisplayName("getList 메서드 호출 시")
    class DescribeGetList {

        @Test
        @DisplayName("모든 아이돌 목록을 반환한다")
        void itReturnsAllIdols() throws Exception {
            // Given
            ReadIdolResponse idol1 = new ReadIdolResponse(TEST_ID, TEST_NAME, TEST_DATE, TEST_DATE);
            ReadIdolResponse idol2 = new ReadIdolResponse(2L, "테스트 아이돌 2", TEST_DATE, TEST_DATE);
            given(readIdolUseCase.getIdols(0L, 20)).willReturn(List.of(idol1, idol2));

            // When
            ResultActions result = mockMvc.perform(get("/api/v1/idols")
                .with(user("testUser").roles("USER")));

            // Then
            result.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getOne 메서드 호출 시")
    class DescribeGetOne {

        @Nested
        @DisplayName("존재하는 아이돌 ID가 주어지면")
        class WithExistingIdolId {

            @Test
            @DisplayName("아이돌 정보를 반환한다")
            void itReturnsIdol() throws Exception {
                // Given
                ReadIdolResponse idol = new ReadIdolResponse(TEST_ID, TEST_NAME, TEST_DATE,
                    TEST_DATE);
                given(readIdolUseCase.getIdol(TEST_ID)).willReturn(idol);

                // When
                ResultActions result = mockMvc.perform(get("/api/v1/idols/{id}", TEST_ID)
                    .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 아이돌 ID가 주어지면")
        class WithNonExistingIdolId {

            @Test
            @DisplayName("404 에러를 반환한다")
            void itReturns404Error() throws Exception {
                // Given
                given(readIdolUseCase.getIdol(TEST_ID))
                    .willThrow(new NotFoundException("존재하지 않는 아이돌입니다. id=" + TEST_ID));

                // When
                ResultActions result = mockMvc.perform(get("/api/v1/idols/{id}", TEST_ID)
                    .with(user("testUser").roles("USER")));

                // Then
                result.andExpect(status().isNotFound());
            }
        }
    }
} 
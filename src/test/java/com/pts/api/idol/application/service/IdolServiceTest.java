package com.pts.api.idol.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadIdolResponse;
import com.pts.api.idol.application.port.out.IdolRepositoryPort;
import com.pts.api.idol.domain.model.Idol;
import com.pts.api.common.base.BaseUnitTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@DisplayName("IdolService 클래스")
class IdolServiceTest extends BaseUnitTest {

    @Mock
    private IdolRepositoryPort idolRepositoryPort;

    private IdolService idolService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 아이돌";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        idolService = new IdolService(idolRepositoryPort);
    }

    @Nested
    @DisplayName("getIdol 메서드 호출 시")
    class DescribeGetIdol {

        @Nested
        @DisplayName("존재하는 아이돌 ID가 주어지면")
        class WithExistingIdolId {

            @Test
            @DisplayName("아이돌 정보를 반환한다")
            void itReturnsIdol() {
                // Given
                Idol idol = Idol.builder()
                    .id(TEST_ID)
                    .name(TEST_NAME)
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                when(idolRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.of(idol));

                // When
                ReadIdolResponse response = idolService.getIdol(TEST_ID);

                // Then
                assertThat(response.id()).isEqualTo(TEST_ID);
                assertThat(response.name()).isEqualTo(TEST_NAME);
                assertThat(response.createdAt()).isEqualTo(TEST_DATE);
                assertThat(response.updatedAt()).isEqualTo(TEST_DATE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 아이돌 ID가 주어지면")
        class WithNonExistingIdolId {

            @Test
            @DisplayName("NotFoundException이 발생한다")
            void itThrowsNotFoundException() {
                // Given
                when(idolRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> idolService.getIdol(TEST_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 아이돌입니다. id=" + TEST_ID);
            }
        }
    }

    @Nested
    @DisplayName("getIdols 메서드 호출 시")
    class DescribeGetIdols {

        @Test
        @DisplayName("모든 아이돌 목록을 반환한다")
        void itReturnsAllIdols() {
            // Given
            Idol idol1 = Idol.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            Idol idol2 = Idol.builder()
                .id(2L)
                .name("테스트 아이돌 2")
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            when(idolRepositoryPort.findAll(0L, 20))
                .thenReturn(List.of(idol1, idol2));

            // When
            List<ReadIdolResponse> responses = idolService.getIdols(0L, 20);

            // Then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(TEST_ID);
            assertThat(responses.get(0).name()).isEqualTo(TEST_NAME);
            assertThat(responses.get(1).id()).isEqualTo(2L);
            assertThat(responses.get(1).name()).isEqualTo("테스트 아이돌 2");
        }
    }
} 
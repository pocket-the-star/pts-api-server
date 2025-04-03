package com.pts.api.idol.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.pts.api.global.common.exception.NotFoundException;
import com.pts.api.idol.application.dto.response.ReadArtistResponse;
import com.pts.api.idol.application.port.out.ArtistRepositoryPort;
import com.pts.api.idol.domain.model.Artist;
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

@DisplayName("ArtistService 클래스")
class ArtistServiceTest extends BaseUnitTest {

    @Mock
    private ArtistRepositoryPort artistRepositoryPort;

    private ArtistService artistService;

    private static final Long TEST_IDOL_ID = 1L;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "테스트 아티스트";
    private static final LocalDateTime TEST_DATE = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        artistService = new ArtistService(artistRepositoryPort);
    }

    @Nested
    @DisplayName("getArtist 메서드 호출 시")
    class DescribeGetArtist {

        @Nested
        @DisplayName("존재하는 아티스트 ID가 주어지면")
        class WithExistingArtistId {

            @Test
            @DisplayName("아티스트 정보를 반환한다")
            void itReturnsArtist() {
                // Given
                Idol idol = Idol.builder()
                    .id(TEST_IDOL_ID)
                    .name("테스트 아이돌")
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                
                Artist artist = Artist.builder()
                    .id(TEST_ID)
                    .name(TEST_NAME)
                    .idol(idol)
                    .createdAt(TEST_DATE)
                    .updatedAt(TEST_DATE)
                    .build();
                
                when(artistRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.of(artist));

                // When
                ReadArtistResponse response = artistService.getArtist(TEST_ID);

                // Then
                assertThat(response.id()).isEqualTo(TEST_ID);
                assertThat(response.name()).isEqualTo(TEST_NAME);
                assertThat(response.createdAt()).isEqualTo(TEST_DATE);
                assertThat(response.updatedAt()).isEqualTo(TEST_DATE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 아티스트 ID가 주어지면")
        class WithNonExistingArtistId {

            @Test
            @DisplayName("NotFoundException이 발생한다")
            void itThrowsNotFoundException() {
                // Given
                when(artistRepositoryPort.findOneById(TEST_ID))
                    .thenReturn(Optional.empty());

                // When & Then
                assertThatThrownBy(() -> artistService.getArtist(TEST_ID))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("존재하지 않는 아티스트입니다. id=" + TEST_ID);
            }
        }
    }

    @Nested
    @DisplayName("getArtists 메서드 호출 시")
    class DescribeGetArtists {

        @Test
        @DisplayName("모든 아티스트 목록을 반환한다")
        void itReturnsAllArtists() {
            // Given
            Idol idol = Idol.builder()
                .id(TEST_IDOL_ID)
                .name("테스트 아이돌")
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            
            Artist artist1 = Artist.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .idol(idol)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            
            Artist artist2 = Artist.builder()
                .id(2L)
                .name("테스트 아티스트 2")
                .idol(idol)
                .createdAt(TEST_DATE)
                .updatedAt(TEST_DATE)
                .build();
            
            when(artistRepositoryPort.findAll(TEST_IDOL_ID))
                .thenReturn(List.of(artist1, artist2));

            // When
            List<ReadArtistResponse> responses = artistService.getArtists(TEST_IDOL_ID);

            // Then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).id()).isEqualTo(TEST_ID);
            assertThat(responses.get(0).name()).isEqualTo(TEST_NAME);
            assertThat(responses.get(0).createdAt()).isEqualTo(TEST_DATE);
            assertThat(responses.get(0).updatedAt()).isEqualTo(TEST_DATE);
            assertThat(responses.get(1).id()).isEqualTo(2L);
            assertThat(responses.get(1).name()).isEqualTo("테스트 아티스트 2");
            assertThat(responses.get(1).createdAt()).isEqualTo(TEST_DATE);
            assertThat(responses.get(1).updatedAt()).isEqualTo(TEST_DATE);
        }
    }
} 
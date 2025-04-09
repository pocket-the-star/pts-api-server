package com.pts.api.idol.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.idol.application.dto.response.ReadArtistResponse;
import com.pts.api.idol.application.port.in.ReadArtistUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "아이돌", description = "아이돌 API")
@RestController
@RequestMapping("/api/v1/idols")
@RequiredArgsConstructor
public class ReadArtistController {

    private final ReadArtistUseCase readArtistUseCase;

    @Operation(summary = "아이돌 아티스트 리스트 불러오기", description = "아이돌 아티스트 리스트 불러오기 API")
    @GetMapping("/{idolId}/artists")
    public ResponseEntity<BaseResponse<List<ReadArtistResponse>>> getList(
        @PathVariable Long idolId) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readArtistUseCase.getArtists(idolId));
    }

    @Operation(summary = "아이돌 멤버 불러오기", description = "아이돌 멤버 불러오기 API")
    @GetMapping("/{idolId}/artists/{id}")
    public ResponseEntity<BaseResponse<ReadArtistResponse>> getOne(@PathVariable Long id) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK, readArtistUseCase.getArtist(id));
    }

}

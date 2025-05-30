package com.pts.api.idol.presentation.controller;

import com.pts.api.global.presentation.response.BaseResponse;
import com.pts.api.global.presentation.response.ResponseGenerator;
import com.pts.api.global.presentation.response.ResponseMsg;
import com.pts.api.idol.application.dto.response.ReadIdolResponse;
import com.pts.api.idol.application.port.in.ReadIdolUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "아이돌", description = "아이돌 API")
@RestController
@RequestMapping("/api/v1/idols")
@RequiredArgsConstructor
public class ReadIdolController {

    private final ReadIdolUseCase readIdolUseCase;

    @Operation(summary = "아이돌 리스트 불러오기", description = "아이돌 리스트 불러오기 API")
    @GetMapping()
    public ResponseEntity<BaseResponse<List<ReadIdolResponse>>> getList(
        @RequestParam(value = "offset", defaultValue = "0") Long offset,
        @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readIdolUseCase.getIdols(offset, limit));
    }

    @Operation(summary = "아이돌 불러오기", description = "아이돌 불러오기 API")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ReadIdolResponse>> getOne(
        @PathVariable Long id
    ) {
        return ResponseGenerator.ok(ResponseMsg.OK, HttpStatus.OK,
            readIdolUseCase.getIdol(id));
    }


}

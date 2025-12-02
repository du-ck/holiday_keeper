package com.example.holidaykeeper.interfaces.api.holiday;


import com.example.holidaykeeper.application.facade.HolidayFacade;
import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.interfaces.api.dto.DeleteHoliday;
import com.example.holidaykeeper.interfaces.api.dto.RefreshHoliday;
import com.example.holidaykeeper.interfaces.api.dto.SearchHoliday;
import com.example.holidaykeeper.interfaces.api.dto.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Holiday", description = "공휴일 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/holidays")
public class HolidayController {

    private final HolidayFacade holidayFacade;

    /**
     * 데이터 적재 기능
     *
     * 최근 5 년(2020 ~ 2025)의 공휴일을 외부 API에서 수집하여 저장
     * 최초 실행시 시 5 년 × N 개 국가를 일괄 적재하는 기능 포함
     */
    @Operation(summary = "데이터 적재", description = "최근 5 년의 공휴일을 외부 API에서 수집하여 저장합니다, 국가코드도 같이 저장됩니다.")
    @PostMapping("/load")
    @ApiResponse(responseCode = "200", description = "데이터 적재 성공",
            content = @Content(
                    mediaType = "application/json",
                    schemaProperties = {
                            @SchemaProperty(name= "success", schema = @Schema(example = "true")),
                            @SchemaProperty(name="data", schema = @Schema(example = "ok"))
                    }
            )
    )
    public ResponseEntity<ResponseData> load() throws Exception {

        boolean result = holidayFacade.loadHolidaysWithHistory();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(result)
                .data(result ? "ok" : "fail")
                .build(), result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 검색 기능
     *
     * 연도별·국가별 필터 기반 공휴일 조회
     * from ~ to 기간, 공휴일 타입 등 추가 필터 자유 확장
     * 결과는 페이징 형태로 응답
     */
    @Operation(summary = "공휴일 검색", description = "여러 조건을 통해 공휴일 검색이 가능합니다. 페이징형태로 응답합니다.")
    @GetMapping("")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schemaProperties = {
                            @SchemaProperty(name= "success", schema = @Schema(example = "true")),
                            @SchemaProperty(name="data", schema = @Schema(implementation = SearchHolidayFacade.Response.class))
                    }
            )
    )
    public ResponseEntity<ResponseData> holidays(@Valid SearchHoliday.Request req) throws Exception {

        List<SearchHolidayFacade.Response> results = holidayFacade.searchHoliday(SearchHoliday.toFacadeDto(req));

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .data(results)
                .build(), HttpStatus.OK);
    }

    /**
     * 재동기화(Refresh) 기능
     *
     * 특정 연도·국가 데이터를 재호출하여 Upsert (덮어쓰기) 가능
     */
    @Operation(summary = "데이터 재동기화", description = "특정 연도, 국가코드를 기준으로 공휴일 데이터를 재동기화 합니다.")
    @PutMapping("/refresh")
    @ApiResponse(responseCode = "200", description = "재동기화 성공",
            content = @Content(
                    mediaType = "application/json",
                    schemaProperties = {
                            @SchemaProperty(name= "success", schema = @Schema(example = "true")),
                            @SchemaProperty(name="data", schema = @Schema(example = "ok"))
                    }
            )
    )
    public ResponseEntity<ResponseData> refresh(@Valid @RequestBody RefreshHoliday.Request req) throws Exception {

        boolean result = holidayFacade.refreshHoliday(RefreshHoliday.toFacadeDto(req));

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .data(result ? "ok" : "fail")
                .build(), HttpStatus.OK);
    }

    /**
     * 삭제 기능
     *
     * 특정 연도·국가의 공휴일 레코드 전체 삭제
     */
    @Operation(summary = "공휴일 데이터 삭제", description = "특정 연도, 국가코드를 기준으로 공휴일 데이터를 삭제합니다.")
    @DeleteMapping("/delete")
    @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(
                    mediaType = "application/json",
                    schemaProperties = {
                            @SchemaProperty(name= "success", schema = @Schema(example = "true")),
                            @SchemaProperty(name="data", schema = @Schema(example = "ok"))
                    }
            )
    )
    public ResponseEntity<ResponseData> delete(@Valid @ModelAttribute DeleteHoliday.Request req) throws Exception {

        boolean result = holidayFacade.deleteHoliday(DeleteHoliday.toFacadeDto(req));

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(result)
                .data("ok")
                .build(), HttpStatus.OK);
    }
}

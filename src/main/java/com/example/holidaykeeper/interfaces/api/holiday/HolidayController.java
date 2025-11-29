package com.example.holidaykeeper.interfaces.api.holiday;


import com.example.holidaykeeper.interfaces.api.dto.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/holidays")
public class HolidayController {

    /**
     * 데이터 적재 기능
     *
     * 최근 5 년(2020 ~ 2025)의 공휴일을 외부 API에서 수집하여 저장
     * 최초 실행시 시 5 년 × N 개 국가를 일괄 적재하는 기능 포함
     */
    @PostMapping("/load")
    public ResponseEntity<ResponseData> load() throws Exception {

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data("ok")
                .build(), HttpStatus.OK);
    }

    /**
     * 검색 기능
     *
     * 연도별·국가별 필터 기반 공휴일 조회
     * from ~ to 기간, 공휴일 타입 등 추가 필터 자유 확장
     * 결과는 페이징 형태로 응답
     */
    @GetMapping("")
    public ResponseEntity<ResponseData> holidays() throws Exception {

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data("ok")
                .build(), HttpStatus.OK);
    }

    /**
     * 재동기화(Refresh) 기능
     *
     * 특정 연도·국가 데이터를 재호출하여 Upsert (덮어쓰기) 가능
     */
    @PutMapping("/refresh")
    public ResponseEntity<ResponseData> refresh() throws Exception {

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data("ok")
                .build(), HttpStatus.OK);
    }

    /**
     * 삭제 기능
     *
     * 특정 연도·국가의 공휴일 레코드 전체 삭제
     */
    @DeleteMapping("/{year}/{countryCode}")
    public ResponseEntity<ResponseData> delete() throws Exception {

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data("ok")
                .build(), HttpStatus.OK);
    }
}

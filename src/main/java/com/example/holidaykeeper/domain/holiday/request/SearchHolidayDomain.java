package com.example.holidaykeeper.domain.holiday.request;

import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class SearchHolidayDomain {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer year;
        private Integer month;
        private List<HolidayTypeEnum> types;
        private String countryCode;

        private LocalDate fromDate; // from~to 필터용
        private LocalDate toDate;   // from~to 필터용

        private boolean isGlobal;

        private int page; // 요청 페이지 번호 (0부터 시작)
        private int size; // 페이지당 항목 수
    }
}

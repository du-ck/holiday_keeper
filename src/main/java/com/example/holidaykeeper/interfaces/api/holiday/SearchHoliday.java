package com.example.holidaykeeper.interfaces.api.holiday;

import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchHoliday {
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer year;
        private Integer month;
        private List<HolidayTypeEnum> types = Collections.singletonList(HolidayTypeEnum.PUBLIC);
        private String countryCode;

        private String fromDate; // from~to 필터용
        private String toDate;   // from~to 필터용

        private boolean isGlobal = true;

        private int page = 0; // 요청 페이지 번호 (0부터 시작)
        private int size = 10; // 페이지당 항목 수
    }

    @Builder
    @Getter
    public static class Response {
        private LocalDate holidayDate;
        private String countryCode;
        private String countryName;
        private String localName;
        private String englishName;
        private boolean global;
        private Integer launchYear;
        private List<String> countyNames;
        private List<HolidayTypeEnum> types;
    }

    public static SearchHolidayFacade.Request toFacadeDto(SearchHoliday.Request dto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");

        LocalDate fromDate = Objects.isNull(dto.getFromDate()) ? null :
                YearMonth.parse(dto.getFromDate(), formatter).atDay(1);

        LocalDate toDate = Objects.isNull(dto.getToDate()) ? null :
                YearMonth.parse(dto.getToDate(), formatter).atEndOfMonth();

        return SearchHolidayFacade.Request.builder()
                .year(dto.getYear())
                .month(dto.getMonth())
                .types(dto.getTypes())
                .countryCode(dto.getCountryCode())
                .fromDate(fromDate)
                .toDate(toDate)
                .isGlobal(dto.isGlobal())
                .page(dto.getPage())
                .size(dto.getSize())
                .build();
    }
}

package com.example.holidaykeeper.interfaces.api.dto;

import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import jakarta.validation.constraints.Pattern;
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

        @Pattern(regexp = "^\\d{6}$")
        private String fromDate; // from~to 필터용
        @Pattern(regexp = "^\\d{6}$")
        private String toDate;   // from~to 필터용

        private Boolean isGlobal;

        private int page = 0; // 요청 페이지 번호 (0부터 시작)
        private int size = 10; // 페이지당 항목 수
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
                .isGlobal(dto.getIsGlobal() == null ? true : false)
                .page(dto.getPage())
                .size(dto.getSize())
                .build();
    }
}

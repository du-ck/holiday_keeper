package com.example.holidaykeeper.interfaces.api.dto;

import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchHoliday {

    @Schema(description = "공휴일 검색 요청 정보를 담는 DTO")
    @ParameterObject
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "연", example = "2025", minLength = 4, maxLength = 4)
        private Integer year;
        @Schema(description = "월", example = "3", minLength = 1, maxLength = 2)
        private Integer month;
        @Schema(description = "공휴일 타입", example = "PUBLIC", allowableValues = {"PUBLIC", "BANK", "SCHOOL", "AUTHORITIES", "OPTIONAL", "OBSERVANCE"})
        private List<HolidayTypeEnum> types = Collections.singletonList(HolidayTypeEnum.PUBLIC);
        @Schema(description = "국가 코드", example = "KR")
        private String countryCode;

        @Schema(description = "from 날짜", example = "2021")
        @Pattern(regexp = "^\\d{6}$")
        private String fromDate; // from~to 필터용

        @Schema(description = "to 날짜", example = "2025")
        @Pattern(regexp = "^\\d{6}$")
        private String toDate;   // from~to 필터용

        @Schema(description = "전역 여부", example = "true", defaultValue = "true")
        private Boolean isGlobal;

        @Schema(description = "요청 페이지 번호", example = "0", defaultValue = "0")
        private Integer page; // 요청 페이지 번호 (0부터 시작)
        @Schema(description = "페이지당 항목 수", example = "10", defaultValue = "10")
        private Integer size; // 페이지당 항목 수
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
                .page(dto.getPage() == null? 0 : dto.getPage())
                .size(dto.getSize() == null? 10 : dto.getSize())
                .build();
    }
}

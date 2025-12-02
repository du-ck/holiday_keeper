package com.example.holidaykeeper.application.facade.request;

import com.example.holidaykeeper.domain.holiday.HolidayDetail;
import com.example.holidaykeeper.domain.holiday.HolidayType;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.example.holidaykeeper.infra.holiday.HolidayTypeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class SearchHolidayFacade {

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

        private int page;
        private int size;
    }

    @Schema(description = "공휴일 검색 결과정보를 담는 DTO")
    @Builder
    @Getter
    public static class Response {
        @Schema(description = "공휴일 날짜", example = "2025-08-15")
        private LocalDate holidayDate;

        @Schema(description = "국가 코드", example = "KR")
        private String countryCode;

        @Schema(description = "국가 이름", example = "South Korea")
        private String countryName;

        @Schema(description = "지역 이름", example = "광복절")
        private String localName;

        @Schema(description = "영어 이름", example = "Liberation Day")
        private String englishName;

        @Schema(description = "전역 여부", example = "true", defaultValue = "true")
        private boolean global;

        @Schema(description = "시행 날짜", example = "null")
        private Integer launchYear;

        @Schema(description = "자치주")
        private List<String> countyNames;

        @Schema(description = "공휴일 타입", example = "PUBLIC", allowableValues = {"PUBLIC", "BANK", "SCHOOL", "AUTHORITIES", "OPTIONAL", "OBSERVANCE"})
        private List<HolidayTypeEnum> types;
    }

    public static SearchHolidayDomain.Request toDomainDto(SearchHolidayFacade.Request facadeDto) {
        return SearchHolidayDomain.Request.builder()
                .year(facadeDto.getYear())
                .month(facadeDto.getMonth())
                .types(facadeDto.getTypes())
                .countryCode(facadeDto.getCountryCode())
                .fromDate(facadeDto.getFromDate())
                .toDate(facadeDto.getToDate())
                .isGlobal(facadeDto.isGlobal())
                .page(facadeDto.getPage())
                .size(facadeDto.getSize())
                .build();
    }

    public static SearchHolidayFacade.Response toFacadeDto(HolidayDetail domainDto) {
        return SearchHolidayFacade.Response.builder()
                .holidayDate(domainDto.getHolidayDate())
                .countryCode(domainDto.getCountryCode())
                .countryName(domainDto.getCountryName())
                .localName(domainDto.getLocalName())
                .englishName(domainDto.getEngName())
                .global(domainDto.getIsGlobal())
                .launchYear(domainDto.getLaunchYear())
                .countyNames(domainDto.getCountyNames())
                .types(domainDto.getTypes())
                .build();
    }

    public static List<SearchHolidayFacade.Response> toFacadeDtoList(List<HolidayDetail> domainDtoList) {
        return domainDtoList.stream().map(m -> toFacadeDto(m)).toList();
    }
}

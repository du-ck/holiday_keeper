package com.example.holidaykeeper.application.facade.request;

import com.example.holidaykeeper.domain.holiday.HolidayDetail;
import com.example.holidaykeeper.domain.holiday.HolidayType;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.example.holidaykeeper.infra.holiday.HolidayTypeEntity;
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

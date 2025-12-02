package com.example.holidaykeeper.application.facade.request;

import com.example.holidaykeeper.domain.holiday.HolidayDetail;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import com.example.holidaykeeper.domain.holiday.request.RefreshHolidayDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class RefreshHolidayFacade {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer year;
        private String countryCode;
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

    public static RefreshHolidayDomain.Request toDomainDto(RefreshHolidayFacade.Request facadeDto) {
        return RefreshHolidayDomain.Request.builder()
                .year(facadeDto.getYear())
                .countryCode(facadeDto.getCountryCode())
                .build();
    }

    public static RefreshHolidayFacade.Response toFacadeDto(HolidayDetail domainDto) {
        return RefreshHolidayFacade.Response.builder()
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

    public static List<RefreshHolidayFacade.Response> toFacadeDtoList(List<HolidayDetail> domainDtoList) {
        return domainDtoList.stream().map(m -> toFacadeDto(m)).toList();
    }
}

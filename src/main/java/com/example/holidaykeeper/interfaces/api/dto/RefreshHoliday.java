package com.example.holidaykeeper.interfaces.api.dto;

import com.example.holidaykeeper.application.facade.request.RefreshHolidayFacade;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class RefreshHoliday {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private Integer year;
        @NotNull
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

    public static RefreshHolidayFacade.Request toFacadeDto(RefreshHoliday.Request dto) {
        return RefreshHolidayFacade.Request.builder()
                .year(dto.getYear())
                .countryCode(dto.getCountryCode())
                .build();
    }
}

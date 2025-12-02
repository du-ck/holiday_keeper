package com.example.holidaykeeper.domain.holiday.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
public class RefreshHolidayDomain {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Integer year;
        private String countryCode;
    }
}

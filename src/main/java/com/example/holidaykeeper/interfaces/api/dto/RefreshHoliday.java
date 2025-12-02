package com.example.holidaykeeper.interfaces.api.dto;

import com.example.holidaykeeper.application.facade.request.RefreshHolidayFacade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
public class RefreshHoliday {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "연", example = "2025", minLength = 4, maxLength = 4)
        @NotNull
        private Integer year;

        @Schema(description = "국가 코드", example = "KR")
        @NotNull
        private String countryCode;
    }

    public static RefreshHolidayFacade.Request toFacadeDto(RefreshHoliday.Request dto) {
        return RefreshHolidayFacade.Request.builder()
                .year(dto.getYear())
                .countryCode(dto.getCountryCode())
                .build();
    }
}

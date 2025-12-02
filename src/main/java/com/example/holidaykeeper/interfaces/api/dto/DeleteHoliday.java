package com.example.holidaykeeper.interfaces.api.dto;

import com.example.holidaykeeper.application.facade.request.DeleteHolidayFacade;
import com.example.holidaykeeper.application.facade.request.RefreshHolidayFacade;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class DeleteHoliday {

    @Builder
    @Getter
    @Setter
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


    public static DeleteHolidayFacade.Request toFacadeDto(DeleteHoliday.Request dto) {
        return DeleteHolidayFacade.Request.builder()
                .year(dto.getYear())
                .countryCode(dto.getCountryCode())
                .build();
    }
}

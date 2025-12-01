package com.example.holidaykeeper.domain.holiday;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class Holiday {
    private Long id;
    private LocalDate date;
    private String localName;
    @JsonProperty("name")
    private String englishName;
    private String countryCode;
    private String countryName;
    private boolean global;             // 모든 주의 공휴일인지 여부
    private List<String> counties;      // global이 false 일 경우 연방 주 공휴일이여서 리스트로 들어옴.
    private Integer launchYear;         // 공휴일 시작 연도
    private List<HolidayTypeEnum> types;     //공휴일 유형

}

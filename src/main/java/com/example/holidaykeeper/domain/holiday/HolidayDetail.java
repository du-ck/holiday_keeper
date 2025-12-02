package com.example.holidaykeeper.domain.holiday;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDetail {

    // Holiday 필드
    private Long id;
    private String localName;
    private String engName;
    private LocalDate holidayDate;
    private Integer year;
    private Integer month;
    private String countryCode;
    private Boolean isGlobal;
    private Integer launchYear;
    private String countryName;
    private List<String> countyNames;
    private List<HolidayTypeEnum> types;

    // 시간 정보
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

    // QueryDSL Projections용 생성자
    public HolidayDetail(
            Long id,
            String localName,
            String engName,
            LocalDate holidayDate,
            Integer year,
            Integer month,
            String countryCode,
            Boolean isGlobal,
            Integer launchYear,
            String countryName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt,
            Boolean isDeleted
    ) {
        this.id = id;
        this.localName = localName;
        this.engName = engName;
        this.holidayDate = holidayDate;
        this.year = year;
        this.month = month;
        this.countryCode = countryCode;
        this.isGlobal = isGlobal;
        this.launchYear = launchYear;
        this.countryName = countryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }
}

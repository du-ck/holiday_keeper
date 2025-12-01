package com.example.holidaykeeper.domain.holiday;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HolidayTypeEnum {
    @JsonProperty("Public")
    PUBLIC,
    @JsonProperty("Bank")
    BANK,
    @JsonProperty("School")
    SCHOOL,
    @JsonProperty("Authorities")
    AUTHORITIES,
    @JsonProperty("Optional")
    OPTIONAL,
    @JsonProperty("Observance")
    OBSERVANCE
}

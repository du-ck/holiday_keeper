package com.example.holidaykeeper.domain.holiday;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class Country {

    @JsonProperty("countryCode")
    private String code;
    private String name;
}

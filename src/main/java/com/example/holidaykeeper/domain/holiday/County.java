package com.example.holidaykeeper.domain.holiday;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class County {
    private long holidayId;
    private String code;
    private String name;
}

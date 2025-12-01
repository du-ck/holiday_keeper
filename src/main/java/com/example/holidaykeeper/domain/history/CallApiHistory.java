package com.example.holidaykeeper.domain.history;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class CallApiHistory {

    private String endpoint;
    private String httpMethod;
    private String countryCode;
    private Integer requestYear;
    private String requestParams;
    private Integer responseStatus;
    private String responseBody;
    private int responseTimeMs;
    private boolean isSuccess;
    private String errorMessage;
}

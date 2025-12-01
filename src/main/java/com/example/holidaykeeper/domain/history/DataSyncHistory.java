package com.example.holidaykeeper.domain.history;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class DataSyncHistory {
    private String countryCode;
    private Integer syncYear;
    private OperationTypeEnum operationType;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}

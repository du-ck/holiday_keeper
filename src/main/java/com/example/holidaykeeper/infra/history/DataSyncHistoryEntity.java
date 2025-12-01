package com.example.holidaykeeper.infra.history;

import com.example.holidaykeeper.domain.history.DataSyncHistory;
import com.example.holidaykeeper.domain.history.OperationTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "data_sync_history")
public class DataSyncHistoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "sync_year", nullable = false)
    private Integer syncYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", length = 15, nullable = false)
    private OperationTypeEnum operationType;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;


    public static DataSyncHistory toDomain(DataSyncHistoryEntity entity) {
        return DataSyncHistory.builder()
                .countryCode(entity.getCountryCode())
                .syncYear(entity.getSyncYear())
                .operationType(entity.getOperationType())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

    public static List<DataSyncHistory> toDomainList(List<DataSyncHistoryEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static DataSyncHistoryEntity toEntity(DataSyncHistory domain) {
        return DataSyncHistoryEntity.builder()
                .countryCode(domain.getCountryCode())
                .syncYear(domain.getSyncYear())
                .operationType(domain.getOperationType())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .build();
    }

    public static List<DataSyncHistoryEntity> toEntityList(List<DataSyncHistory> domainList) {
        return domainList.stream().map(m -> DataSyncHistoryEntity.toEntity(m)).toList();
    }
}

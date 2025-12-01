package com.example.holidaykeeper.infra.history;

import com.example.holidaykeeper.domain.history.OperationTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private OperationTypeEnum type;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;
}

package com.example.holidaykeeper.infra.history;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "call_api_history")
public class CallApiHistoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endpoint", nullable = false, length = 500)
    private String endpoint;

    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "request_year", nullable = false)
    private Integer requestYear;

    @Column(name = "request_params", nullable = false, columnDefinition = "TEXT")
    private String requestParams;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "response_time_ms", nullable = false)
    private Integer responseTimeMs;

    @Column(name = "is_success", nullable = false)
    private boolean isSuccess;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
    }
}

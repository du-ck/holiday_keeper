package com.example.holidaykeeper.infra.history;

import com.example.holidaykeeper.domain.history.CallApiHistory;
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

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "request_year")
    private Integer requestYear;

    @Column(name = "request_params", columnDefinition = "TEXT")
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

    public static CallApiHistory toDomain(CallApiHistoryEntity entity) {
        return CallApiHistory.builder()
                .endpoint(entity.getEndpoint())
                .httpMethod(entity.getHttpMethod())
                .countryCode(entity.getCountryCode())
                .requestYear(entity.getRequestYear())
                .requestParams(entity.getRequestParams())
                .responseStatus(entity.getResponseStatus())
                .responseBody(entity.getResponseBody())
                .responseTimeMs(entity.getResponseTimeMs())
                .isSuccess(entity.isSuccess())
                .errorMessage(entity.getErrorMessage())
                .build();
    }

    public static CallApiHistoryEntity toEntity(CallApiHistory domain) {
        return CallApiHistoryEntity.builder()
                .endpoint(domain.getEndpoint())
                .httpMethod(domain.getHttpMethod())
                .countryCode(domain.getCountryCode())
                .requestYear(domain.getRequestYear())
                .requestParams(domain.getRequestParams())
                .responseStatus(domain.getResponseStatus())
                .responseBody(domain.getResponseBody())
                .responseTimeMs(domain.getResponseTimeMs())
                .isSuccess(domain.isSuccess())
                .errorMessage(domain.getErrorMessage())
                .build();
    }
}

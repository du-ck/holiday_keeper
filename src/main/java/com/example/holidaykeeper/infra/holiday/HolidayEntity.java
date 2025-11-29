package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.HolidayType;
import com.example.holidaykeeper.infra.country.CountryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "holiday",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_holiday",
                columnNames = {"country_code", "holiday_date", "eng_name"}
        ))
public class HolidayEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "year_value", insertable = false, updatable = false)
    private Integer year_value;

    @Column(name = "month_value", insertable = false, updatable = false)
    private Integer month_value;

    @Column(name = "local_name", nullable = false, length = 200)
    private String localName;

    @Column(name = "eng_name", nullable = false, length = 200)
    private String engName;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private HolidayType type;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", insertable = false, updatable = false)
    private CountryEntity country;


    @PrePersist
    protected void onCreate() {
        calculateYearMonth();
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        calculateYearMonth();
        updatedAt = LocalDateTime.now();
    }

    private void calculateYearMonth() {
        if (holidayDate != null) {
            this.year_value = holidayDate.getYear();
            this.month_value = holidayDate.getMonthValue();
        }
    }
}

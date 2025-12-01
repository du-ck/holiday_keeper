package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.Holiday;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "holiday",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_holiday",
                columnNames = {"country_code", "holiday_date", "eng_name", "local_name", "is_deleted"}
        ))
public class HolidayEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "year_value")
    private Integer year;

    @Column(name = "month_value")
    private Integer month;

    @Column(name = "local_name", nullable = false, length = 200)
    private String localName;

    @Column(name = "eng_name", nullable = false, length = 200)
    private String engName;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(name = "is_global", nullable = false)
    @Builder.Default
    private boolean isGlobal = false;

    @Column(name = "launch_year")
    private Integer launchYear;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
            this.year = holidayDate.getYear();
            this.month = holidayDate.getMonthValue();
        }
    }

    public static Holiday toDomain(HolidayEntity entity) {
        return Holiday.builder()
                .id(entity.getId())
                .date(entity.getHolidayDate())
                .localName(entity.getLocalName())
                .englishName(entity.getEngName())
                .countryCode(entity.getCountryCode())
                .global(entity.isGlobal())
                .launchYear(entity.getLaunchYear())
                .build();
    }

    public static List<Holiday> toDomainList(List<HolidayEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static HolidayEntity toEntity(Holiday domain) {
        return HolidayEntity.builder()
                .holidayDate(domain.getDate())
                .localName(domain.getLocalName())
                .engName(domain.getEnglishName())
                .countryCode(domain.getCountryCode())
                .isGlobal(domain.isGlobal())
                .launchYear(domain.getLaunchYear())
                .build();
    }

    public static List<HolidayEntity> toEntityList(List<Holiday> domainList) {
        return domainList.stream().map(m -> HolidayEntity.toEntity(m)).toList();
    }
}

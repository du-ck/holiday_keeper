package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.HolidayType;
import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "holiday_type")
public class HolidayTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_id", nullable = false)
    private Long holidayId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 30, nullable = false)
    private HolidayTypeEnum type;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
    }

    public static HolidayType toDomain(HolidayTypeEntity entity) {
        return HolidayType.builder()
                .holidayId(entity.getHolidayId())
                .type(entity.getType())
                .build();
    }

    public static List<HolidayType> toDomainList(List<HolidayTypeEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static HolidayTypeEntity toEntity(HolidayType domain) {
        return HolidayTypeEntity.builder()
                .holidayId(domain.getHolidayId())
                .type(domain.getType())
                .build();
    }

    public static List<HolidayTypeEntity> toEntityList(List<HolidayType> domainList) {
        return domainList.stream().map(m -> HolidayTypeEntity.toEntity(m)).toList();
    }
}

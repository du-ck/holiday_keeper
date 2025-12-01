package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.County;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "county")
public class CountyEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_id", nullable = false)
    private Long holidayId;

    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "name", length = 30)
    private String name;

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

    public static County toDomain(CountyEntity entity) {
        return County.builder()
                .holidayId(entity.getHolidayId())
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }

    public static List<County> toDomainList(List<CountyEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static CountyEntity toEntity(County domain) {
        return CountyEntity.builder()
                .holidayId(domain.getHolidayId())
                .code(domain.getCode())
                .name(domain.getName())
                .build();
    }

    public static List<CountyEntity> toEntityList(List<County> domainList) {
        return domainList.stream().map(m -> CountyEntity.toEntity(m)).toList();
    }
}

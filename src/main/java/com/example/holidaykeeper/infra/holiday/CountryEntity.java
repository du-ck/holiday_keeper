package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Country;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "country")
public class CountryEntity {
    @Id
    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
    }

    public static Country toDomain(CountryEntity entity) {
        return Country.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .build();
    }

    public static List<Country> toDomainList(List<CountryEntity> entities) {
        return entities.stream().map(m -> toDomain(m)).toList();
    }

    public static CountryEntity toEntity(Country domain) {
        return CountryEntity.builder()
                .code(domain.getCode())
                .name(domain.getName())
                .build();
    }

    public static List<CountryEntity> toEntityList(List<Country> domainList) {
        return domainList.stream().map(m -> CountryEntity.toEntity(m)).toList();
    }
}

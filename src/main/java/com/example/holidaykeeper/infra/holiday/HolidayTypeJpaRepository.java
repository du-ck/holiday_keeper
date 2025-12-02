package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayTypeJpaRepository extends JpaRepository<HolidayTypeEntity, Long> {
    List<HolidayTypeEntity> findByHolidayIdIn(List<Long> holidayIds);
}

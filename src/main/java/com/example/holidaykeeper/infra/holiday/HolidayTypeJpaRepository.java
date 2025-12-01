package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayTypeJpaRepository extends JpaRepository<HolidayTypeEntity, Long> {
}

package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.County;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountyJpaRepository extends JpaRepository<CountyEntity, Long> {
    List<CountyEntity> findByHolidayIdIn(List<Long> holidayIds);
}

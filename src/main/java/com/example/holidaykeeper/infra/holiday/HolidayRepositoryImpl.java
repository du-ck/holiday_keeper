package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.domain.holiday.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HolidayRepositoryImpl implements HolidayRepository {

    private final HolidayJpaRepository jpaRepository;


    @Override
    public List<Holiday> saveAll(List<Holiday> holidays) {
        List<HolidayEntity> savedEntities = jpaRepository.saveAll(HolidayEntity.toEntityList(holidays));
        return HolidayEntity.toDomainList(savedEntities);
    }
}

package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.HolidayType;
import com.example.holidaykeeper.domain.holiday.HolidayTypeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HolidayTypeRepositoryImpl implements HolidayTypeRepository {

    private final HolidayTypeJpaRepository jpaRepository;
    private final EntityManager entityManager;

    @Override
    public List<HolidayType> saveAll(List<HolidayType> holidayTypes) {
        List<HolidayTypeEntity> savedEntities = jpaRepository.saveAll(HolidayTypeEntity.toEntityList(holidayTypes));
        return HolidayTypeEntity.toDomainList(savedEntities);
    }

    @Override
    public List<HolidayType> findByHolidayIdIn(List<Long> holidayIds) {
        List<HolidayTypeEntity> results = jpaRepository.findByHolidayIdIn(holidayIds);
        return HolidayTypeEntity.toDomainList(results);
    }

    /**
     * 대량이라 JPQL로 처리
     */
    @Override
    public boolean deleteAll() {
        jpaRepository.updateAllIsDeletedTrue();
        entityManager.clear();
        return true;
    }
}

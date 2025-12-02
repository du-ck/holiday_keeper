package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountyRepositoryImpl implements CountyRepository {

    private final CountyJpaRepository jpaRepository;
    private final EntityManager entityManager;

    @Override
    public List<County> saveAll(List<County> counties) {
        List<CountyEntity> savedEntities = jpaRepository.saveAll(CountyEntity.toEntityList(counties));
        return CountyEntity.toDomainList(savedEntities);
    }

    @Override
    public List<County> findByHolidayIdIn(List<Long> holidayIds) {
        List<CountyEntity> results = jpaRepository.findByHolidayIdIn(holidayIds);
        return CountyEntity.toDomainList(results);
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

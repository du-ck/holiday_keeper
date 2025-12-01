package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountyRepositoryImpl implements CountyRepository {

    private final CountyJpaRepository jpaRepository;

    @Override
    public List<County> saveAll(List<County> counties) {
        List<CountyEntity> savedEntities = jpaRepository.saveAll(CountyEntity.toEntityList(counties));
        return CountyEntity.toDomainList(savedEntities);
    }

}

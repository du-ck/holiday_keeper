package com.example.holidaykeeper.infra.holiday;

import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final CountryJpaRepository jpaRepository;

    @Override
    public List<Country> saveAll(List<Country> countries) {
        List<CountryEntity> savedEntities = jpaRepository.saveAll(CountryEntity.toEntityList(countries));
        return CountryEntity.toDomainList(savedEntities);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}

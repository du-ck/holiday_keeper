package com.example.holidaykeeper.infra.holiday;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryJpaRepository extends JpaRepository<CountryEntity, Long> {
}

package com.example.holidaykeeper.domain.holiday;

import java.util.List;
import java.util.Optional;

public interface CountryRepository {

    List<Country> saveAll(List<Country> countries);
    void deleteAll();
}

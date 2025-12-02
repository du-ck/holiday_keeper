package com.example.holidaykeeper.domain.holiday;

import java.util.List;

public interface CountyRepository {

    List<County> saveAll(List<County> counties);

    List<County> findByHolidayIdIn(List<Long> holidayIds);
    boolean deleteAll();
    int delete(List<Long> ids);
}

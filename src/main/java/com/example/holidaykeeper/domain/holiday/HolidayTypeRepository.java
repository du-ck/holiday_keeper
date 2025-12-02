package com.example.holidaykeeper.domain.holiday;

import java.util.List;

public interface HolidayTypeRepository {

    List<HolidayType> saveAll(List<HolidayType> holidayTypes);
    List<HolidayType> findByHolidayIdIn(List<Long> holidayIds);
    boolean deleteAll();
    int delete(List<Long> ids);
}

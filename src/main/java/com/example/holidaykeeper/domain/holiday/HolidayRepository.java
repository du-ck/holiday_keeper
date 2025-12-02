package com.example.holidaykeeper.domain.holiday;

import java.util.List;

import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayRepository {
    List<Holiday> saveAll(List<Holiday> holidays);
    Page<HolidayDetail> searchHolidays(SearchHolidayDomain.Request req, Pageable pageable);
    boolean deleteAll();

    int delete(List<Long> ids);
    List<Holiday> searchHolidayIds(int year, String countryCode);
}

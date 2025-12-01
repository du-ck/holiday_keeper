package com.example.holidaykeeper.domain.holiday;

import java.util.List;

public interface HolidayRepository {
    List<Holiday> saveAll(List<Holiday> holidays);
}

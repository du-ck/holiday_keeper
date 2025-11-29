package com.example.holidaykeeper.domain.holiday;

public enum HolidayType {
    PUBLIC,
    BANK, // (Bank holiday, banks and offices are closed)
    SCHOOL, //(School holiday, schools are closed)
    AUTHORITIES, // (Authorities are closed)
    OPTIONAL, // (Majority of people take a day off)
    OBSERVANCE // (Optional festivity, no paid day off)
}

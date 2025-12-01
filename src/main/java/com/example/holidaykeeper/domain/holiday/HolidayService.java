package com.example.holidaykeeper.domain.holiday;


import com.example.holidaykeeper.support.api.HolidayApiCaller;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;
    private final CountyRepository countyRepository;
    private final HolidayTypeRepository holidayTypeRepository;

    private final HolidayApiCaller holidayApiCaller;

    public boolean load() throws Exception {

        long startTime = System.currentTimeMillis();

        List<Country> countries = holidayApiCaller.loadCountries();
        List<Holiday> uniqueHolidays = holidayApiCaller.loadHolidays();

        saveHolidaysToDatabase(countries, uniqueHolidays);

        long duration = System.currentTimeMillis() - startTime;

        log.info("Holidays Total: {}, Duration: {}ms ({}s)",
                uniqueHolidays.size(), duration, duration / 1000);

        return true;
    }

    /**
     * 공휴일 정보 목록을 받아 DB에 저장한다.
     */
    @Transactional
    public boolean saveHolidaysToDatabase(List<Country> countries, List<Holiday> holidays) {

        // 국가코드 정보 저장
        // 국가코드는 따로 isDeleted 처리 X, load 시에만 전체 삭제 후 saveAll
        countryRepository.deleteAll();
        countryRepository.saveAll(countries);

        // holiday_id를 가져오기 위함.
        List<Holiday> dbResult = holidayRepository.saveAll(holidays);

        // dbResult를 countryCode|date|englishName|localName → Holiday 매핑
        Map<String, Holiday> holidayMap = dbResult.stream()
                .collect(Collectors.toMap(
                        h -> h.getCountryCode() + "|" + h.getDate() + "|" + h.getEnglishName() + "|" + h.getLocalName(),
                        h -> h
                ));

        List<County> counties = new ArrayList<>();
        List<HolidayType> holidayTypes = new ArrayList<>();

        holidays.stream()
                .forEach(h -> {
                    String key = h.getCountryCode() + "|" + h.getDate() + "|" + h.getEnglishName() + "|" + h.getLocalName();
                    Holiday dbHoliday = holidayMap.get(key);
                    if (dbHoliday == null) {
                        return;
                    }

                    // counties 처리
                    if (h.getCounties() != null && !h.getCounties().isEmpty()) {
                        h.getCounties().forEach(countyName -> {
                            counties.add(County.builder()
                                    .holidayId(dbHoliday.getId())
                                    .code(dbHoliday.getCountryCode())
                                    .name(countyName)
                                    .build());
                        });
                    }

                    // types 처리
                    if (h.getTypes() != null && !h.getTypes().isEmpty()) {
                        h.getTypes().forEach(type -> {
                            holidayTypes.add(HolidayType.builder()
                                    .holidayId(dbHoliday.getId())
                                    .type(type)
                                    .build());
                        });
                    }
                });

        // county 정보와 holidayType 정보 저장
        if (!counties.isEmpty()) {
            countyRepository.saveAll(counties);
        }
        if (!holidayTypes.isEmpty()) {
            holidayTypeRepository.saveAll(holidayTypes);
        }

        return true;

    }
}




package com.example.holidaykeeper.domain.holiday;


import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.example.holidaykeeper.support.api.HolidayApiCaller;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import com.example.holidaykeeper.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public boolean deleteHolidayDataAll() {
        countyRepository.deleteAll();
        holidayTypeRepository.deleteAll();
        holidayRepository.deleteAll();
        return true;
    }

    public boolean deleteHolidayData(List<Long> holidayIds) {
        holidayRepository.delete(holidayIds);
        holidayTypeRepository.delete(holidayIds);
        countyRepository.delete(holidayIds);
        return true;
    }

    public List<HolidayDetail> searchHoliday(SearchHolidayDomain.Request req) throws Exception {
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        // Holiday 정보
        List<HolidayDetail> result = holidayRepository.searchHolidays(req, pageable).getContent();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        List<Long> holidayIds = result.stream()
                .map(HolidayDetail::getId)
                .collect(Collectors.toList());

        //holiday_id를 기준으로 county, type 값 가져오기
        List<County> counties = countyRepository.findByHolidayIdIn(holidayIds);
        List<HolidayType> types = holidayTypeRepository.findByHolidayIdIn(holidayIds);

        // County 매핑
        Map<Long, List<String>> countyMap = counties.stream()
                .collect(Collectors.groupingBy(
                        County::getHolidayId,
                        Collectors.mapping(County::getName, Collectors.toList())
                ));

        // Type 매핑
        Map<Long, List<HolidayTypeEnum>> typeMap = types.stream()
                .collect(Collectors.groupingBy(
                        HolidayType::getHolidayId,
                        Collectors.mapping(HolidayType::getType, Collectors.toList())
                ));


        List<HolidayDetail> finalResults = result.stream()
                .map(detail -> detail.toBuilder()
                        .countyNames(countyMap.getOrDefault(detail.getId(), List.of()))
                        .types(typeMap.getOrDefault(detail.getId(), List.of()))
                        .build())
                .collect(Collectors.toList());

        return finalResults;
    }

    public List<Holiday> searchHolidayIds(int year, String countryCode) {
        return holidayRepository.searchHolidayIds(year, countryCode);
    }


    /**
     * 국가 정보 로드
     */
    public List<Country> loadCountries() throws Exception {
        List<Country> result = holidayApiCaller.loadCountries();
        if (result.isEmpty()) {
            throw new ApiCallFailedException("API 통신결과가 없습니다.");
        }
        return result;
    }

    /**
     * 공휴일 정보 로드
     */
    public List<Holiday> loadHolidays() throws Exception {
        List<Holiday> result = holidayApiCaller.loadHolidays();
        if (result.isEmpty()) {
            throw new ApiCallFailedException("API 통신결과가 없습니다.");
        }
        return result;
    }

    /**
     * 공휴일 정보 로드 (year, countryCode 기준)
     */
    public List<Holiday> loadHolidays(Integer year, String countryCode) throws Exception {
        int intYear = year.intValue();
        List<Holiday> result = holidayApiCaller.loadHolidays(intYear, countryCode);
        if (result.isEmpty()) {
            throw new ApiCallFailedException("API 통신결과가 없습니다.");
        }
        return result;
    }

    /**
     * 공휴일 정보 목록을 받아 DB에 저장한다.
     * 호출하는곳에서 @Transactional 적용
     */
    public List<Holiday> saveHolidaysToDatabase(List<Country> countries, List<Holiday> holidays) {

        // 국가코드 정보 저장
        // 국가코드는 따로 isDeleted 처리 X, load 시에만 전체 삭제 후 saveAll
        if (!countries.isEmpty()) {
            countryRepository.deleteAll();
            countryRepository.saveAll(countries);
        }

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
        return dbResult;
    }
}




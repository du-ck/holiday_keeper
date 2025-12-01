package com.example.holidaykeeper.domain.holiday;

import com.example.holidaykeeper.support.api.HolidayApiCaller;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @InjectMocks
    private HolidayService holidayService;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private HolidayTypeRepository holidayTypeRepository;

    @Mock
    private HolidayApiCaller holidayApiCaller;

    private List<Country> testCountries;
    private List<Holiday> testHolidays;
    private List<Holiday> dbHolidaysResult;


    @BeforeEach
    void setUp() {
        testCountries = Arrays.asList(
                new Country("KR", "South Korea"),
                new Country("US", "United States")
        );

        Holiday holidayKR = Holiday.builder()
                .id(1L)
                .countryCode("KR")
                .date(LocalDate.of(2025, 1, 1))
                .englishName("New Year's Day")
                .localName("새해")
                .counties(Collections.emptyList())
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .build();

        Holiday holidayUS = Holiday.builder()
                .id(2L)
                .countryCode("US")
                .date(LocalDate.of(2025, 7, 4))
                .englishName("Independence Day")
                .localName("Independence Day")
                .counties(Arrays.asList("AL", "NY"))
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .build();

        testHolidays = Arrays.asList(holidayKR, holidayUS);

        Holiday dbHolidayKR = Holiday.builder()
                .id(1L)
                .countryCode("KR")
                .date(LocalDate.of(2025, 1, 1))
                .englishName("New Year's Day")
                .localName("새해")
                .counties(Collections.emptyList())
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .build();

        Holiday dbHolidayUS = Holiday.builder()
                .id(2L)
                .countryCode("US")
                .date(LocalDate.of(2025, 7, 4))
                .englishName("Independence Day")
                .localName("Independence Day")
                .counties(Arrays.asList("AL", "NY"))
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .build();

        dbHolidaysResult = Arrays.asList(dbHolidayKR, dbHolidayUS);
    }

    /**
     * 핵심로직 정상기능 테스트
     */
    @Test
    void load() throws Exception {
        given(holidayApiCaller.loadCountries())
                .willReturn(testCountries);
        given(holidayApiCaller.loadHolidays())
                .willReturn(testHolidays);

        given(countryRepository.saveAll(testCountries))
                .willReturn(testCountries);
        given(holidayRepository.saveAll(testHolidays))
                .willReturn(testHolidays);

        boolean result = holidayService.load();

        // load() 메서드의 결과가 true인지 확인
        assertTrue(result, "load 메서드는 성공 시 true를 반환해야 합니다.");

    }

    @Test
    @DisplayName("API 호출 실패 시 예외 발생")
    void apiCallFail_ThrowsException() throws Exception {

        given(holidayApiCaller.loadCountries())
                .willThrow(new ApiCallFailedException("API 통신에 실패했습니다."));


        Exception exception = assertThrows(ApiCallFailedException.class, () -> holidayService.load());

        Assertions.assertEquals("API 통신에 실패했습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 리스트도 정상 처리")
    void load_EmptyList() throws Exception {

        given(holidayApiCaller.loadCountries())
                .willReturn(Collections.emptyList());
        given(holidayApiCaller.loadHolidays())
                .willReturn(Collections.emptyList());

        given(countryRepository.saveAll(anyList()))
                .willReturn(Collections.emptyList());
        given(holidayRepository.saveAll(anyList()))
                .willReturn(Collections.emptyList());


        boolean result = holidayService.load();

        assertTrue(result);
        verify(countryRepository).saveAll(Collections.emptyList());
        verify(holidayRepository).saveAll(Collections.emptyList());
    }

    @Test
    @DisplayName("counties와 types가 null인 경우")
    void counties_types_null() {

        Holiday holidayWithNulls = Holiday.builder()
                .id(3L)
                .countryCode("JP")
                .date(LocalDate.of(2025, 1, 1))
                .englishName("New Year")
                .localName("元日")
                .counties(null)  // null
                .types(null)      // null
                .build();

        List<Holiday> holidaysWithNulls = Collections.singletonList(holidayWithNulls);

        given(countryRepository.saveAll(anyList())).willReturn(testCountries);
        given(holidayRepository.saveAll(holidaysWithNulls)).willReturn(holidaysWithNulls);


        boolean result = holidayService.saveHolidaysToDatabase(testCountries, holidaysWithNulls);

        assertTrue(result);
    }

    @Test
    @DisplayName("DB 제약 위반")
    void dbFail_Unique() {

        given(countryRepository.saveAll(anyList()))
                .willThrow(new DataIntegrityViolationException("DB 제약 위반"));

        assertThrows(DataIntegrityViolationException.class,
                () -> holidayService.saveHolidaysToDatabase(testCountries, testHolidays));
    }

}

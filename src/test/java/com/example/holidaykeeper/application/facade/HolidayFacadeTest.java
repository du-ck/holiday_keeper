package com.example.holidaykeeper.application.facade;

import com.example.holidaykeeper.application.facade.request.DeleteHolidayFacade;
import com.example.holidaykeeper.application.facade.request.RefreshHolidayFacade;
import com.example.holidaykeeper.domain.history.HistoryService;
import com.example.holidaykeeper.domain.holiday.*;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HolidayFacadeTest {

    @InjectMocks
    private HolidayFacade holidayFacade;

    @Mock
    private HolidayService holidayService;

    @Mock
    private HistoryService historyService;

    private List<Country> testCountries;
    private List<Holiday> testHolidays;
    private List<Holiday> dbHolidaysResult;
    private List<HolidayDetail> searchHolidayResult;


    @BeforeEach
    void setUp() {
        testCountries = Arrays.asList(
                Country.builder()
                        .code("KR")
                        .name("South Korea")
                        .build(),
                Country.builder()
                        .code("US")
                        .name("United States")
                        .build()
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

        HolidayDetail holidayDetail1 = HolidayDetail.builder()
                .id(1L)
                .holidayDate(LocalDate.of(2025, 6, 6))
                .engName("Memorial Day")
                .localName("현충일")
                .countryCode("KR")
                .countryName("South Korea")
                .isGlobal(true)
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .launchYear(null)
                .countyNames(List.of())
                .build();

        HolidayDetail holidayDetail2 = HolidayDetail.builder()
                .id(1L)
                .holidayDate(LocalDate.of(2025, 8, 15))
                .engName("Liberation Day")
                .localName("광복절")
                .countryCode("KR")
                .countryName("South Korea")
                .isGlobal(true)
                .types(Arrays.asList(HolidayTypeEnum.PUBLIC))
                .launchYear(null)
                .countyNames(List.of())
                .build();

        searchHolidayResult = Arrays.asList(holidayDetail1, holidayDetail2);
    }

    @Test
    @DisplayName("load로직 정상기능 테스트")
    void load() throws Exception {

        given(holidayService.loadCountries())
                .willReturn(testCountries);
        given(holidayService.loadHolidays(5))
                .willReturn(testHolidays);

        given(holidayService.saveHolidaysToDatabase(testCountries, testHolidays))
                .willReturn(dbHolidaysResult);

        boolean result = holidayFacade.loadHolidaysWithHistory();

        // load() 메서드의 결과가 true인지 확인
        assertTrue(result, "load 메서드는 성공 시 true를 반환해야 합니다.");

    }

    @Test
    @DisplayName("API 호출 실패 시 예외 발생")
    void apiCallFail_ThrowsException() throws Exception {

        given(holidayService.loadCountries())
                .willThrow(new ApiCallFailedException("API 통신에 실패했습니다."));


        Exception exception = assertThrows(ApiCallFailedException.class, () -> holidayFacade.loadHolidaysWithHistory());

        Assertions.assertEquals("API 통신에 실패했습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 리스트도 정상 처리")
    void load_EmptyList() throws Exception {

        given(holidayService.loadCountries())
                .willReturn(Collections.emptyList());
        given(holidayService.loadHolidays(5))
                .willReturn(Collections.emptyList());

        given(holidayService.saveHolidaysToDatabase(anyList(), anyList()))
                .willReturn(Collections.emptyList());


        boolean result = holidayFacade.loadHolidaysWithHistory();

        assertTrue(result);

        verify(holidayService).saveHolidaysToDatabase(Collections.emptyList(), Collections.emptyList());

        verify(historyService).saveSyncHistories(anyList());
    }


    @Test
    @DisplayName("refresh 로직 정상기능 테스트")
    void refresh() throws Exception {
        int year = 2025;
        String countryCode = "KR";

        RefreshHolidayFacade.Request req = RefreshHolidayFacade.Request.builder()
                .year(year)
                .countryCode(countryCode)
                .build();

        given(holidayService.loadHolidays(year, countryCode))
                .willReturn(testHolidays);
        given(holidayService.searchHolidayIds(year, countryCode))
                .willReturn(testHolidays);

        given(holidayService.saveHolidaysToDatabase(List.of(), testHolidays))
                .willReturn(dbHolidaysResult);

        //holidayService.searchHoliday
        given(holidayService.searchHoliday(any(SearchHolidayDomain.Request.class))).willReturn(searchHolidayResult);

        boolean result = holidayFacade.refreshHoliday(req);

        assertTrue(result);
    }

    @Test
    @DisplayName("delete 로직 정상기능 테스트")
    void delete() {
        int year = 2025;
        String countryCode = "KR";

        DeleteHolidayFacade.Request req = DeleteHolidayFacade.Request.builder()
                .year(year)
                .countryCode(countryCode)
                .build();

        given(holidayService.searchHolidayIds(year, countryCode))
                .willReturn(testHolidays);

        given(holidayService.deleteHolidayData(anyList()))
                .willReturn(true);

        boolean result = holidayFacade.deleteHoliday(req);

        assertTrue(result);
        verify(holidayService, times(1)).deleteHolidayData(anyList());
    }
}

package com.example.holidaykeeper.application.facade;

import com.example.holidaykeeper.domain.history.HistoryService;
import com.example.holidaykeeper.domain.holiday.*;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
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
    }

    /**
     * 핵심로직 정상기능 테스트
     */
    @Test
    void load() throws Exception {

        given(holidayService.loadCountries())
                .willReturn(testCountries);
        given(holidayService.loadHolidays())
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
        given(holidayService.loadHolidays())
                .willReturn(Collections.emptyList());

        given(holidayService.saveHolidaysToDatabase(anyList(), anyList()))
                .willReturn(Collections.emptyList());


        boolean result = holidayFacade.loadHolidaysWithHistory();

        assertTrue(result);

        verify(holidayService).saveHolidaysToDatabase(Collections.emptyList(), Collections.emptyList());

        verify(historyService).saveSyncHistories(anyList());
    }

}

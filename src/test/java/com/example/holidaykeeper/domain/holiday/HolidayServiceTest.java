package com.example.holidaykeeper.domain.holiday;

import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import com.example.holidaykeeper.interfaces.api.dto.SearchHoliday;
import com.example.holidaykeeper.support.api.HolidayApiCaller;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import com.example.holidaykeeper.support.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {

    @InjectMocks
    private HolidayService holidayService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private HolidayTypeRepository holidayTypeRepository;

    @Mock
    private HolidayApiCaller holidayApiCaller;

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

        List<Holiday> result = holidayService.saveHolidaysToDatabase(testCountries, holidaysWithNulls);

        assertNotNull(result, "반환된 결과는 null이 아니어야 합니다.");
        assertEquals(1, result.size(), "저장된 공휴일 수는 1개여야 합니다.");
    }

    @Test
    @DisplayName("DB 제약 위반")
    void dbFail_Unique() {

        given(countryRepository.saveAll(anyList()))
                .willThrow(new DataIntegrityViolationException("DB 제약 위반"));

        assertThrows(DataIntegrityViolationException.class,
                () -> holidayService.saveHolidaysToDatabase(testCountries, testHolidays));
    }


    @Test
    @DisplayName("Holiday 조회로직 정상작동 테스트")
    void searchHoliday() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        int page = 0;
        int size = 10;

        LocalDate fromDate = YearMonth.parse("202506", formatter).atDay(1);
        LocalDate toDate = YearMonth.parse("202508", formatter).atEndOfMonth();

        List<HolidayTypeEnum> types = Collections.singletonList(HolidayTypeEnum.PUBLIC);
        SearchHolidayFacade.Request req = new SearchHolidayFacade.Request(null, null, types,
                "KR", fromDate, toDate, false, page, size);

        SearchHolidayDomain.Request domainReq = SearchHolidayFacade.toDomainDto(req);
        Pageable pageable = PageRequest.of(domainReq.getPage(), domainReq.getSize());

        Page<HolidayDetail> result = new PageImpl<>(
                searchHolidayResult,
                pageable,
                2L
        );

        given(holidayRepository.searchHolidays(domainReq, pageable))
                .willReturn(result);
        given(countyRepository.findByHolidayIdIn(anyList())).willReturn(List.of());
        given(holidayTypeRepository.findByHolidayIdIn(anyList())).willReturn(List.of());


        List<HolidayDetail> test = holidayService.searchHoliday(domainReq);
        assertEquals(2, test.size(), "결과는 2개가 나와야합니다");
    }

    /**
     * parameter가 하나도 안들어와도
     * 기본값 설정 > 전체조회 형태로 정상 진행되야함
     * (page = 0, size = 10, type = PUBLIC, global = true)
     */
    @Test
    @DisplayName("parameter 모두 null")
    void searchHoliday_parameter_null() throws Exception {
        SearchHoliday.Request searchDto = new SearchHoliday.Request();  //여기서 기본값 설정됨.
        SearchHolidayDomain.Request req = SearchHolidayFacade.toDomainDto(SearchHoliday.toFacadeDto(searchDto));

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        Page<HolidayDetail> searchResult = new PageImpl<>(
                searchHolidayResult,
                pageable,
                2L
        );

        given(holidayRepository.searchHolidays(req, pageable))
                .willReturn(searchResult);
        given(countyRepository.findByHolidayIdIn(anyList())).willReturn(List.of());
        given(holidayTypeRepository.findByHolidayIdIn(anyList())).willReturn(List.of());

        List<HolidayDetail> test = holidayService.searchHoliday(req);
        assertEquals(2, test.size(), "결과는 2개가 나와야합니다");
    }

    @Test
    @DisplayName("조회결과 없으면 ResourceNotFoundException 발생")
    void search_resourceNotFound() throws Exception {
        SearchHoliday.Request searchDto = new SearchHoliday.Request();  //여기서 기본값 설정됨.
        SearchHolidayDomain.Request req = SearchHolidayFacade.toDomainDto(SearchHoliday.toFacadeDto(searchDto));

        Pageable pageable = PageRequest.of(req.getPage(), req.getSize());

        Page<HolidayDetail> searchResult = new PageImpl<>(
                List.of(),
                pageable,
                0L
        );

        given(holidayRepository.searchHolidays(req, pageable))
                .willReturn(searchResult);

        assertThrows(ResourceNotFoundException.class,
                () -> holidayService.searchHoliday(req));
    }

    @Test
    @DisplayName("refresh 기능 중 nager api 데이터가 없을 경우")
    void refresh_nager_NoData() throws Exception {
        int year = 2025;
        String countryCode = "KR";

        given(holidayApiCaller.loadHolidays(year, countryCode))
                .willReturn(Collections.emptyList());

        assertThrows(ApiCallFailedException.class,
                () -> holidayService.loadHolidays(year, countryCode));
    }
}

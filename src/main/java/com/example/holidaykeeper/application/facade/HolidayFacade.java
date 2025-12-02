package com.example.holidaykeeper.application.facade;

import com.example.holidaykeeper.application.facade.request.DeleteHolidayFacade;
import com.example.holidaykeeper.application.facade.request.RefreshHolidayFacade;
import com.example.holidaykeeper.application.facade.request.SearchHolidayFacade;
import com.example.holidaykeeper.domain.history.DataSyncHistory;
import com.example.holidaykeeper.domain.history.HistoryService;
import com.example.holidaykeeper.domain.history.OperationTypeEnum;
import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.domain.holiday.HolidayDetail;
import com.example.holidaykeeper.domain.holiday.HolidayService;
import com.example.holidaykeeper.domain.holiday.request.SearchHolidayDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HolidayFacade {

    private final HistoryService historyService;
    private final HolidayService holidayService;

    /**
     * 공휴일 로드 + 이력 저장을 함께 처리
     */
    @Transactional
    public boolean loadHolidaysWithHistory() throws Exception {
        LocalDateTime startedAt = LocalDateTime.now();

        //HolidayService를 통한 데이터 로드
        List<Country> countries = holidayService.loadCountries();
        List<Holiday> holidays = holidayService.loadHolidays();

        // 기존 데이터 delete 처리 (소프트 딜리트)
        holidayService.deleteHolidayDataAll();

        // Holiday 저장
        holidayService.saveHolidaysToDatabase(countries, holidays);

        // HistoryService 를 통한 이력 생성 및 저장
        List<DataSyncHistory> syncHistories = createSyncHistories(holidays, startedAt, OperationTypeEnum.LOAD);
        historyService.saveSyncHistories(syncHistories);

        return true;
    }

    /**
     * holiday도메인의 dto와 history 도메인의 dto를 동시사용 -> application 단
     * 변경 이력 생성
     */
    public List<DataSyncHistory> createSyncHistories(List<Holiday> holidays, LocalDateTime startedAt,
                                                     OperationTypeEnum operationType) {

        // 국가코드-연도 조합을 중복 제거하여 수집
        Set<String> countryYearSet = holidays.stream()
                .map(h -> h.getCountryCode() + "-" + h.getDate().getYear())
                .collect(Collectors.toSet());

        LocalDateTime completedAt = LocalDateTime.now();

        // 이력 생성
        List<DataSyncHistory> histories = countryYearSet.stream()
                .map(key -> {
                    String[] parts = key.split("-");
                    return DataSyncHistory.builder()
                            .countryCode(parts[0])
                            .syncYear(Integer.parseInt(parts[1]))
                            .operationType(operationType)
                            .startedAt(startedAt)
                            .completedAt(completedAt)
                            .build();
                })
                .collect(Collectors.toList());

        return histories;
    }

    /**
     * Holiday 조회
     */
    public List<SearchHolidayFacade.Response> searchHoliday(SearchHolidayFacade.Request req) throws Exception {
        List<HolidayDetail> results = holidayService.searchHoliday(SearchHolidayFacade.toDomainDto(req));
        return SearchHolidayFacade.toFacadeDtoList(results);
    }

    /**
     * 재동기화(Refresh) 기능
     *
     * 특정 연도·국가 데이터를 재호출하여 Upsert (덮어쓰기) 가능
     */
    @Transactional
    public List<RefreshHolidayFacade.Response> refreshHoliday(RefreshHolidayFacade.Request req) throws Exception {
        LocalDateTime startedAt = LocalDateTime.now();

        // 연도, 국가 기준으로 nager api 호출.
        List<Holiday> holidays = holidayService.loadHolidays(req.getYear(), req.getCountryCode());

        // 연도, 국가 parameter 기준으로 해당조건의 기존 데이터 조회
        List<Holiday> alreadyData = holidayService.searchHolidayIds(req.getYear(), req.getCountryCode());

        List<Long> holidayIds = alreadyData.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        // holiday, holiday Type, county 소프트딜리트
        holidayService.deleteHolidayData(holidayIds);

        // holiday data 저장
        holidayService.saveHolidaysToDatabase(List.of(), holidays);

        // datasync history 이력 남겨야함 refresh로.
        List<DataSyncHistory> syncHistories = createSyncHistories(holidays, startedAt, OperationTypeEnum.REFRESH);
        historyService.saveSyncHistories(syncHistories);

        List<HolidayDetail> result = holidayService.searchHoliday(SearchHolidayDomain.Request.builder()
                        .year(req.getYear())
                        .countryCode(req.getCountryCode())
                        .size(holidays.size())
                        .build()
        );

        return RefreshHolidayFacade.toFacadeDtoList(result);
    }

    /**
     * 삭제 기능
     * 특정 연도, 국가코드 기준으로 삭제 기능
     */
    @Transactional
    public boolean deleteHoliday(DeleteHolidayFacade.Request req) {
        LocalDateTime startedAt = LocalDateTime.now();
        int intYear = req.getYear().intValue();

        // 연도, 국가 parameter 기준으로 해당조건의 기존 데이터 조회
        List<Holiday> alreadyData = holidayService.searchHolidayIds(intYear, req.getCountryCode());

        List<Long> holidayIds = alreadyData.stream()
                .map(m -> m.getId())
                .collect(Collectors.toList());

        // holiday, holiday Type, county 소프트딜리트
        holidayService.deleteHolidayData(holidayIds);

        List<DataSyncHistory> syncHistories = createSyncHistories(alreadyData, startedAt, OperationTypeEnum.DELETE);
        historyService.saveSyncHistories(syncHistories);

        return true;
    }
}

package com.example.holidaykeeper.support.batch;

import com.example.holidaykeeper.domain.history.DataSyncHistory;
import com.example.holidaykeeper.domain.history.HistoryService;
import com.example.holidaykeeper.domain.history.OperationTypeEnum;
import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.domain.holiday.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class HolidayLoadTasklet implements Tasklet {

    private final HolidayService holidayService;
    private final HistoryService historyService;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ZoneId zone = ZoneId.of("Asia/Seoul");

        LocalDateTime startedAt = LocalDateTime.now(zone);

        // 국가 정보 조회
        List<Country> countries = holidayService.loadCountries();

        // 2. 금년 + 전년 공휴일을 병렬로 한 번에 로드
        List<Holiday> holidays = holidayService.loadHolidays(2);

        log.info("총 공휴일 수: {}", holidays.size());

        // 기존 데이터 전체 삭제(소프트 딜리트)
        holidayService.deleteHolidayDataAll();

        // 신규 데이터 저장
        List<Holiday> saved = holidayService.saveHolidaysToDatabase(countries, holidays);

        // 이력 생성 및 저장 (HolidayFacade.createSyncHistories 와 동일한 로직)
        List<DataSyncHistory> histories =
                createSyncHistories(saved, startedAt, OperationTypeEnum.BATCH_LOAD);
        historyService.saveSyncHistories(histories);

        log.info("HolidayLoadTasklet 종료 - savedHolidays={}, histories={}",
                saved.size(), histories.size());

        return RepeatStatus.FINISHED;
    }

    private List<DataSyncHistory> createSyncHistories(List<Holiday> holidays,
                                                      LocalDateTime startedAt,
                                                      OperationTypeEnum operationType) {

        log.info("데이터 변경이력 생성 시작");

        // 국가코드-연도 조합 중복 제거
        Set<String> countryYearSet = holidays.stream()
                .map(h -> h.getCountryCode() + "-" + h.getDate().getYear())
                .collect(Collectors.toSet());

        LocalDateTime completedAt = LocalDateTime.now();

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

        log.info("데이터 변경이력 생성 완료 - size={}", histories.size());
        return histories;
    }
}

package com.example.holidaykeeper.support.api;

import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.Holiday;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class HolidayApiCaller {

    private final NagerApiClient nagerApi;

    @Qualifier("holidayLoadExecutor")
    private final TaskExecutor holidayLoadExecutor;

    /**
     * Nager API에서 최근 5년 동안의 모든 공휴일 정보를 병렬로 로드 후 중복 제거
     */
    public List<Holiday> loadHolidays() throws Exception {
        // 국가코드 조회
        List<Country> countries = nagerApi.getAvailableCountries();

        // 최근 5년 연도 계산
        int currentYear = LocalDate.now().getYear();
        List<Integer> recent5Years = IntStream.rangeClosed(0, 4)
                .map(i -> currentYear - i)
                .boxed()
                .collect(Collectors.toList());


        // 병렬 처리: holiday 정보들을 병렬처리로 호출하여 저장한다.
        List<CompletableFuture<List<Holiday>>> futures = new ArrayList<>();

        for (Country country : countries) {
            for (int year : recent5Years) {
                CompletableFuture<List<Holiday>> future = CompletableFuture.supplyAsync(
                        () -> nagerApi.getPublicHolidays(year, country.getCode()),
                        holidayLoadExecutor
                );
                futures.add(future);
            }
        }

        // 모든 병렬 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(10, TimeUnit.MINUTES);

        // 결과 합치기
        List<Holiday> allHolidays = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        log.error("Failed to get result", e);
                        return new ArrayList<Holiday>();
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());

        //중복 제거 (countryCode, date, englishName, localName 조건)
        List<Holiday> uniqueHolidays = new ArrayList<>(
                allHolidays.stream()
                        .collect(Collectors.toMap(
                                h -> h.getCountryCode() + "|" + h.getDate() + "|" + h.getEnglishName() + "|" + h.getLocalName(),
                                h -> h,
                                (existing, replacement) -> existing
                        ))
                        .values()
        );

        return uniqueHolidays;
    }

    /**
     * Nager API에서 국가코드 로드
     */
    public List<Country> loadCountries() {
        return nagerApi.getAvailableCountries();
    }
}

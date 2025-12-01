package com.example.holidaykeeper.support.api;

import com.example.holidaykeeper.domain.holiday.Country;
import com.example.holidaykeeper.domain.holiday.Holiday;
import com.example.holidaykeeper.support.exception.ApiCallFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NagerApiClient {

    private final WebClient webClient;

    // 1. 국가 목록 조회
    public List<Country> getAvailableCountries() {
        String url = "https://date.nager.at/api/v3/AvailableCountries";
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(Country.class)
                    .collectList()
                    .block(Duration.ofSeconds(10)); // 타임아웃
        } catch (WebClientResponseException e) {
            throw new ApiCallFailedException("Nager API에서 오류가 발생했습니다.");

        } catch (WebClientRequestException e) {
            throw new ApiCallFailedException("API 통신에 실패했습니다.");
        }
    }

    // 2. 특정 연도 / 국가의 공휴일 조회
    public List<Holiday> getPublicHolidays(int year, String countryCode) {

        String url = String.format(
                "https://date.nager.at/api/v3/PublicHolidays/%d/%s",
                year,
                countryCode
        );
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(Holiday.class)
                    .collectList()
                    .block(Duration.ofSeconds(10)); // 타임아웃
        } catch (WebClientResponseException e) {
            throw new ApiCallFailedException("Nager API에서 오류가 발생했습니다.");

        } catch (WebClientRequestException e) {
            throw new ApiCallFailedException("API 통신에 실패했습니다.");
        }

    }
}

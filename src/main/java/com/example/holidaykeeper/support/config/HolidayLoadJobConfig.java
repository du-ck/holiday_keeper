package com.example.holidaykeeper.batch.holiday;

import com.example.holidaykeeper.support.batch.HolidayLoadTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HolidayLoadJobConfig {

    public static final String HOLIDAY_LOAD_JOB_NAME = "holidayLoadJob";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobLauncher jobLauncher;
    private final HolidayLoadTasklet holidayLoadTasklet;

    @Bean
    public Job holidayLoadJob() {
        return new JobBuilder(HOLIDAY_LOAD_JOB_NAME, jobRepository)
                .start(holidayLoadStep())
                .build();
    }

    @Bean
    public Step holidayLoadStep() {
        return new StepBuilder("holidayLoadStep", jobRepository)
                .tasklet(holidayLoadTasklet, transactionManager)
                .build();
    }

    /**
     * 매년 1월 2일 01:00 KST에 실행
     * cron: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "0 0 1 2 1 ?", zone = "Asia/Seoul")
    public void runHolidayLoadJob() throws Exception {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        log.info("HolidayLoadJob 스케줄 실행 - startedAt={}", now);

        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("triggeredAt", now.toString())
                .toJobParameters();

        jobLauncher.run(holidayLoadJob(), params);
    }
}
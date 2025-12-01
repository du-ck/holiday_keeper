package com.example.holidaykeeper.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolconfig {

    @Bean(name = "holidayLoadExecutor")
    public ThreadPoolTaskExecutor holidayLoadExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본
        executor.setCorePoolSize(cpuCores * 2);
        // 최대
        executor.setMaxPoolSize(cpuCores * 4);
        // 큐
        executor.setQueueCapacity(1000);

        executor.setThreadNamePrefix("holiday-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(120);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }
}

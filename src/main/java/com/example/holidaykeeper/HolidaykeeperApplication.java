package com.example.holidaykeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class HolidaykeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolidaykeeperApplication.class, args);
    }

}

package com.example.holidaykeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class HolidaykeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolidaykeeperApplication.class, args);
    }

}

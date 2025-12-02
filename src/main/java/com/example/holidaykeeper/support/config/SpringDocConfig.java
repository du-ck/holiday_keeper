package com.example.holidaykeeper.support.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Holiday Keeper API", version = "v1.0.0", description = "전 세계 공휴일 검색 기능을 제공하는 API 입니다",
        contact = @Contact(name = "노원욱", email = "wonuk007@naver.com")))
public class SpringDocConfig {
}

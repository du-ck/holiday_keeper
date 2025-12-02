package com.example.holidaykeeper.support.converter;

import com.example.holidaykeeper.domain.holiday.HolidayTypeEnum;
import io.micrometer.common.util.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HolidayTypeConverter implements Converter<String, HolidayTypeEnum>{

    @Override
    public HolidayTypeEnum convert(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        try {
            return HolidayTypeEnum.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 Type 입니다.");
        }
    }
}

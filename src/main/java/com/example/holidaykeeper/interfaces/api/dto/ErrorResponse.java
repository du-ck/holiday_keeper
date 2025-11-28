package com.example.holidaykeeper.interfaces.api.dto;

public record ErrorResponse(
        boolean isSuccess,
        String code,
        String message
) {
}

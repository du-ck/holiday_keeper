package com.example.holidaykeeper.interfaces.api.dto.response;

public record ErrorResponse(
        boolean isSuccess,
        String code,
        String message
) {
}

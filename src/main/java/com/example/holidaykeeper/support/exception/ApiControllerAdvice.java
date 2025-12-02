package com.example.holidaykeeper.support.exception;

import com.example.holidaykeeper.interfaces.api.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("[{}] ResourceNotFoundException :: {}",Thread.currentThread().getName(), e.getMessage(), e);
        //조회결과가 없는 exception 의 경우 success = true 처리.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(true, "404", "해당하는 데이터가 없습니다."));
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("[{}] NoResourceFoundException :: {}",Thread.currentThread().getName(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(false, "404", "요청한 경로가 존재하지 않습니다."));
    }

    @ExceptionHandler(value = ApiCallFailedException.class)
    public ResponseEntity<ErrorResponse> handleApiCallFailedException(ApiCallFailedException e) {
        log.error("[{}] ApiCallFailedException :: {}", Thread.currentThread().getName(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(false, "503", e.getMessage()));
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("[{}] Data Integrity Violation :: {}", Thread.currentThread().getName(), e.getMessage(), e);

        // unique 위반
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(false, "400",
                "데이터 제약 조건을 위반했습니다. 이미 존재하는 값이거나 형식이 올바르지 않습니다."
        ));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[{}] MethodArgumentNotValidException :: {}", Thread.currentThread().getName(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(false, "400", "요청 정보가 올바르지 않습니다."));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[{}] IllegalArgumentException :: {}", Thread.currentThread().getName(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(false, "400", e.getMessage()));
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[{}] Unexpected Internal Server Error", Thread.currentThread().getName(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(false, "500", "예상치 못한 에러가 발생했습니다."));
    }
}
